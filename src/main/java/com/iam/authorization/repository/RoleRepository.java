package com.iam.authorization.repository;

import com.iam.authorization.model.Role;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface RoleRepository extends ReactiveCrudRepository<Role, UUID> {

    // Basic queries
    Mono<Boolean> existsByRoleName(String roleName);
    Mono<Role> findByRoleName(String roleName);
    Flux<Role> findByIsActive(Boolean isActive);
    Flux<Role> findByIsSystemRole(Boolean isSystemRole);

    // Organization queries
    Flux<Role> findByOrgId(Integer orgId);
    Flux<Role> findByOrgIdAndIsActive(Integer orgId, Boolean isActive);
    Flux<Role> findByDepartmentId(Integer departmentId);
    Flux<Role> findByOrgIdAndDepartmentId(Integer orgId, Integer departmentId);

    // Search queries
    @Query("SELECT * FROM roles WHERE role_name ILIKE :search AND is_active = true")
    Flux<Role> searchByRoleName(String search);

    @Query("SELECT * FROM roles WHERE description ILIKE :search AND is_active = true")
    Flux<Role> searchByDescription(String search);

    // System roles
    @Query("SELECT * FROM roles WHERE is_system_role = true AND is_active = true")
    Flux<Role> findSystemRoles();

    // User role queries
    @Query("SELECT r.* FROM roles r " +
            "JOIN user_roles ur ON r.role_id = ur.role_id " +
            "WHERE ur.user_id = :userId AND ur.is_active = true AND r.is_active = true")
    Flux<Role> findRolesByUserId(UUID userId);
}