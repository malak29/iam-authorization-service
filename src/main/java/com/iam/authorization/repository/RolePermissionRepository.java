package com.iam.authorization.repository;

import com.iam.authorization.model.RolePermission;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface RolePermissionRepository extends ReactiveCrudRepository<RolePermission, UUID> {

    // Basic queries
    Flux<RolePermission> findByRoleId(UUID roleId);
    Flux<RolePermission> findByPermissionId(UUID permissionId);
    Flux<RolePermission> findByRoleIdAndIsActive(UUID roleId, Boolean isActive);
    Flux<RolePermission> findByPermissionIdAndIsActive(UUID permissionId, Boolean isActive);

    // Check if role has permission
    Mono<Boolean> existsByRoleIdAndPermissionIdAndIsActive(UUID roleId, UUID permissionId, Boolean isActive);

    // Find specific role-permission mapping
    Mono<RolePermission> findByRoleIdAndPermissionId(UUID roleId, UUID permissionId);

    // Grant/revoke operations
    @Modifying
    @Query("UPDATE role_permissions SET is_active = false WHERE role_id = :roleId AND permission_id = :permissionId")
    Mono<Integer> revokeRolePermission(UUID roleId, UUID permissionId);

    @Modifying
    @Query("UPDATE role_permissions SET is_active = true WHERE role_id = :roleId AND permission_id = :permissionId")
    Mono<Integer> activateRolePermission(UUID roleId, UUID permissionId);

    // Bulk operations
    @Modifying
    @Query("UPDATE role_permissions SET is_active = false WHERE role_id = :roleId")
    Mono<Integer> revokeAllRolePermissions(UUID roleId);
}