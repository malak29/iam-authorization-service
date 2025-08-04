package com.iam.authorization.service;

import com.iam.authorization.dto.*;
import com.iam.authorization.model.Role;
import com.iam.authorization.repository.RoleRepository;
import com.iam.authorization.repository.PermissionRepository;
import com.iam.authorization.repository.UserRoleRepository;
import com.iam.common.exception.CustomExceptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRoleRepository userRoleRepository;

    public Mono<RoleResponse> createRole(CreateRoleRequest request) {
        log.info("Creating role: {}", request.getRoleName());

        return roleRepository.existsByRoleName(request.getRoleName())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new CustomExceptions.ValidationException(
                                "Role with name '" + request.getRoleName() + "' already exists"));
                    }

                    Role role = Role.builder()
                            .roleId(UUID.randomUUID())
                            .roleName(request.getRoleName())
                            .description(request.getDescription())
                            .orgId(request.getOrgId())
                            .departmentId(request.getDepartmentId())
                            .isSystemRole(request.getIsSystemRole())
                            .isActive(true)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();

                    return roleRepository.save(role);
                })
                .map(RoleResponse::new)
                .doOnSuccess(response -> log.info("Role created successfully: {}", response.getRoleName()));
    }

    public Mono<RoleResponse> getRoleById(UUID roleId) {
        return roleRepository.findById(roleId)
                .switchIfEmpty(Mono.error(new CustomExceptions.UserNotFoundException("Role not found")))
                .flatMap(role -> {
                    // Get permissions for this role
                    Flux<PermissionResponse> permissions = permissionRepository.findPermissionsByRoleId(roleId)
                            .map(PermissionResponse::new);

                    // Get user count for this role
                    Mono<Long> userCount = userRoleRepository.findByRoleIdAndIsActive(roleId, true)
                            .count();

                    return Mono.zip(permissions.collectList(), userCount)
                            .map(tuple -> {
                                RoleResponse response = new RoleResponse(role);
                                response.setPermissions(tuple.getT1());
                                response.setUserCount(tuple.getT2().intValue());
                                return response;
                            });
                });
    }

    public Flux<RoleResponse> getAllRoles() {
        return roleRepository.findByIsActive(true)
                .flatMap(role -> {
                    Mono<Long> userCount = userRoleRepository.findByRoleIdAndIsActive(role.getRoleId(), true)
                            .count();
                    return userCount.map(count -> {
                        RoleResponse response = new RoleResponse(role);
                        response.setUserCount(count.intValue());
                        return response;
                    });
                });
    }

    public Flux<RoleResponse> getRolesByOrganization(Integer orgId) {
        return roleRepository.findByOrgIdAndIsActive(orgId, true)
                .map(RoleResponse::new);
    }

    public Mono<RoleResponse> updateRole(UUID roleId, UpdateRoleRequest request) {
        return roleRepository.findById(roleId)
                .switchIfEmpty(Mono.error(new CustomExceptions.UserNotFoundException("Role not found")))
                .flatMap(role -> {
                    if (request.getRoleName() != null) {
                        role.setRoleName(request.getRoleName());
                    }
                    if (request.getDescription() != null) {
                        role.setDescription(request.getDescription());
                    }
                    if (request.getOrgId() != null) {
                        role.setOrgId(request.getOrgId());
                    }
                    if (request.getDepartmentId() != null) {
                        role.setDepartmentId(request.getDepartmentId());
                    }
                    if (request.getIsActive() != null) {
                        role.setIsActive(request.getIsActive());
                    }
                    role.setUpdatedAt(LocalDateTime.now());

                    return roleRepository.save(role);
                })
                .map(RoleResponse::new);
    }

    public Mono<Void> deleteRole(UUID roleId) {
        return roleRepository.findById(roleId)
                .switchIfEmpty(Mono.error(new CustomExceptions.UserNotFoundException("Role not found")))
                .flatMap(role -> {
                    role.setIsActive(false);
                    role.setUpdatedAt(LocalDateTime.now());
                    return roleRepository.save(role);
                })
                .then();
    }
}