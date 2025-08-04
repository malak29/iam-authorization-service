package com.iam.authorization.service;

import com.iam.authorization.dto.*;
import com.iam.authorization.model.*;
import com.iam.authorization.repository.*;
import com.iam.common.exception.CustomExceptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorizationService {

    private final UserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final ReactiveRedisTemplate<String, String> redisTemplate;

    private static final String PERMISSION_CACHE_KEY = "user:permissions:";
    private static final Duration CACHE_TTL = Duration.ofMinutes(15);

    public Mono<Void> assignRoleToUser(AssignRoleRequest request) {
        log.info("Assigning role {} to user {}", request.getRoleId(), request.getUserId());

        return userRoleRepository.existsByUserIdAndRoleIdAndIsActive(request.getUserId(), request.getRoleId(), true)
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new CustomExceptions.ValidationException("User already has this role"));
                    }

                    UserRole userRole = UserRole.builder()
                            .userRoleId(UUID.randomUUID())
                            .userId(request.getUserId())
                            .roleId(request.getRoleId())
                            .expiresAt(request.getExpiresAt())
                            .isActive(true)
                            .grantedAt(LocalDateTime.now())
                            .build();

                    return userRoleRepository.save(userRole);
                })
                .then(invalidateUserPermissionCache(request.getUserId()))
                .doOnSuccess(unused -> log.info("Role assigned successfully"));
    }

    public Mono<Void> revokeRoleFromUser(UUID userId, UUID roleId) {
        log.info("Revoking role {} from user {}", roleId, userId);

        return userRoleRepository.revokeUserRole(userId, roleId)
                .then(invalidateUserPermissionCache(userId))
                .doOnSuccess(unused -> log.info("Role revoked successfully"));
    }

    public Mono<PermissionCheckResponse> checkUserPermission(CheckPermissionRequest request) {
        String cacheKey = PERMISSION_CACHE_KEY + request.getUserId() + ":" +
                request.getResource() + ":" + request.getAction();

        return redisTemplate.opsForValue().get(cacheKey)
                .map(Boolean::parseBoolean)
                .switchIfEmpty(
                        permissionRepository.hasUserPermission(request.getUserId(), request.getResource(), request.getAction())
                                .flatMap(hasPermission -> {
                                    // Cache the result
                                    return redisTemplate.opsForValue()
                                            .set(cacheKey, hasPermission.toString(), CACHE_TTL)
                                            .thenReturn(hasPermission);
                                })
                )
                .flatMap(hasPermission -> {
                    if (hasPermission) {
                        // Get granting roles for audit
                        return getGrantingRoles(request.getUserId(), request.getResource(), request.getAction())
                                .collectList()
                                .map(grantingRoles -> PermissionCheckResponse.builder()
                                        .userId(request.getUserId())
                                        .resource(request.getResource())
                                        .action(request.getAction())
                                        .hasPermission(true)
                                        .grantingRoles(grantingRoles)
                                        .reason("Permission granted through assigned roles")
                                        .build());
                    } else {
                        return Mono.just(PermissionCheckResponse.builder()
                                .userId(request.getUserId())
                                .resource(request.getResource())
                                .action(request.getAction())
                                .hasPermission(false)
                                .reason("No role grants this permission")
                                .build());
                    }
                });
    }

    public Flux<RoleResponse> getUserRoles(UUID userId) {
        return roleRepository.findRolesByUserId(userId)
                .map(RoleResponse::new);
    }

    public Flux<PermissionResponse> getUserPermissions(UUID userId) {
        return permissionRepository.findPermissionsByUserId(userId)
                .map(PermissionResponse::new);
    }

    public Mono<Void> assignPermissionToRole(UUID roleId, UUID permissionId) {
        return rolePermissionRepository.existsByRoleIdAndPermissionIdAndIsActive(roleId, permissionId, true)
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new CustomExceptions.ValidationException("Role already has this permission"));
                    }

                    RolePermission rolePermission = RolePermission.builder()
                            .rolePermissionId(UUID.randomUUID())
                            .roleId(roleId)
                            .permissionId(permissionId)
                            .isActive(true)
                            .grantedAt(LocalDateTime.now())
                            .build();

                    return rolePermissionRepository.save(rolePermission);
                })
                .then(invalidateRolePermissionCache(roleId))
                .doOnSuccess(unused -> log.info("Permission assigned to role successfully"));
    }

    public Mono<Void> revokePermissionFromRole(UUID roleId, UUID permissionId) {
        return rolePermissionRepository.revokeRolePermission(roleId, permissionId)
                .then(invalidateRolePermissionCache(roleId))
                .doOnSuccess(unused -> log.info("Permission revoked from role successfully"));
    }

    // Helper methods
    private Flux<String> getGrantingRoles(UUID userId, String resource, String action) {
        return roleRepository.findRolesByUserId(userId)
                .filterWhen(role ->
                        permissionRepository.findPermissionsByRoleId(role.getRoleId())
                                .any(permission ->
                                        permission.getResource().equals(resource) &&
                                                permission.getAction().equals(action)
                                )
                )
                .map(Role::getRoleName);
    }

    private Mono<Void> invalidateUserPermissionCache(UUID userId) {
        String pattern = PERMISSION_CACHE_KEY + userId + ":*";
        return redisTemplate.keys(pattern)
                .flatMap(redisTemplate::delete)
                .then();
    }

    private Mono<Void> invalidateRolePermissionCache(UUID roleId) {
        // Find all users with this role and invalidate their caches
        return userRoleRepository.findByRoleIdAndIsActive(roleId, true)
                .flatMap(userRole -> invalidateUserPermissionCache(userRole.getUserId()))
                .then();
    }
}