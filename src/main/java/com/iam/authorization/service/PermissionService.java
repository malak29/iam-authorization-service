package com.iam.authorization.service;

import com.iam.authorization.dto.*;
import com.iam.authorization.model.Permission;
import com.iam.authorization.repository.PermissionRepository;
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
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public Mono<PermissionResponse> createPermission(CreatePermissionRequest request) {
        log.info("Creating permission: {}", request.getPermissionName());

        return permissionRepository.existsByPermissionName(request.getPermissionName())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new CustomExceptions.ValidationException(
                                "Permission with name '" + request.getPermissionName() + "' already exists"));
                    }

                    Permission permission = Permission.builder()
                            .permissionId(UUID.randomUUID())
                            .permissionName(request.getPermissionName())
                            .resource(request.getResource())
                            .action(request.getAction())
                            .description(request.getDescription())
                            .isSystemPermission(request.getIsSystemPermission())
                            .isActive(true)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();

                    return permissionRepository.save(permission);
                })
                .map(PermissionResponse::new)
                .doOnSuccess(response -> log.info("Permission created successfully: {}", response.getPermissionName()));
    }

    public Mono<PermissionResponse> getPermissionById(UUID permissionId) {
        return permissionRepository.findById(permissionId)
                .switchIfEmpty(Mono.error(new CustomExceptions.UserNotFoundException("Permission not found")))
                .map(PermissionResponse::new);
    }

    public Flux<PermissionResponse> getAllPermissions() {
        return permissionRepository.findByIsActive(true)
                .map(PermissionResponse::new);
    }

    public Flux<PermissionResponse> getPermissionsByResource(String resource) {
        return permissionRepository.findByResource(resource)
                .map(PermissionResponse::new);
    }

    public Flux<PermissionResponse> getPermissionsByRoleId(UUID roleId) {
        return permissionRepository.findPermissionsByRoleId(roleId)
                .map(PermissionResponse::new);
    }

    public Mono<Void> deletePermission(UUID permissionId) {
        return permissionRepository.findById(permissionId)
                .switchIfEmpty(Mono.error(new CustomExceptions.UserNotFoundException("Permission not found")))
                .flatMap(permission -> {
                    permission.setIsActive(false);
                    permission.setUpdatedAt(LocalDateTime.now());
                    return permissionRepository.save(permission);
                })
                .then();
    }
}