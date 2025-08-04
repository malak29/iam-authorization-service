package com.iam.authorization.repository;

import com.iam.authorization.model.Permission;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface PermissionRepository extends ReactiveCrudRepository<Permission, UUID> {

    // Basic queries
    Mono<Boolean> existsByPermissionName(String permissionName);
    Mono<Permission> findByPermissionName(String permissionName);
    Flux<Permission> findByIsActive(Boolean isActive);
    Flux<Permission> findByIsSystemPermission(Boolean isSystemPermission);

    // Resource and action queries
    Flux<Permission> findByResource(String resource);
    Flux<Permission> findByAction(String action);
    Flux<Permission> findByResourceAndAction(String resource, String action);

    // Search queries
    @Query("SELECT * FROM permissions WHERE permission_name ILIKE :search AND is_active = true")
    Flux<Permission> searchByPermissionName(String search);

    @Query("SELECT * FROM permissions WHERE resource ILIKE :search AND is_active = true")
    Flux<Permission> searchByResource(String search);

    // Role permission queries
    @Query("SELECT p.* FROM permissions p " +
            "JOIN role_permissions rp ON p.permission_id = rp.permission_id " +
            "WHERE rp.role_id = :roleId AND rp.is_active = true AND p.is_active = true")
    Flux<Permission> findPermissionsByRoleId(UUID roleId);

    // User permission queries (through roles)
    @Query("SELECT DISTINCT p.* FROM permissions p " +
            "JOIN role_permissions rp ON p.permission_id = rp.permission_id " +
            "JOIN user_roles ur ON rp.role_id = ur.role_id " +
            "WHERE ur.user_id = :userId AND ur.is_active = true AND rp.is_active = true AND p.is_active = true")
    Flux<Permission> findPermissionsByUserId(UUID userId);

    // Check user permission
    @Query("SELECT COUNT(*) > 0 FROM permissions p " +
            "JOIN role_permissions rp ON p.permission_id = rp.permission_id " +
            "JOIN user_roles ur ON rp.role_id = ur.role_id " +
            "WHERE ur.user_id = :userId AND p.resource = :resource AND p.action = :action " +
            "AND ur.is_active = true AND rp.is_active = true AND p.is_active = true")
    Mono<Boolean> hasUserPermission(UUID userId, String resource, String action);
}