package com.iam.authorization.repository;

import com.iam.authorization.model.UserRole;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface UserRoleRepository extends ReactiveCrudRepository<UserRole, UUID> {

    // Basic queries
    Flux<UserRole> findByUserId(UUID userId);
    Flux<UserRole> findByRoleId(UUID roleId);
    Flux<UserRole> findByUserIdAndIsActive(UUID userId, Boolean isActive);
    Flux<UserRole> findByRoleIdAndIsActive(UUID roleId, Boolean isActive);

    // Check if user has role
    Mono<Boolean> existsByUserIdAndRoleIdAndIsActive(UUID userId, UUID roleId, Boolean isActive);

    // Find specific user-role mapping
    Mono<UserRole> findByUserIdAndRoleId(UUID userId, UUID roleId);

    // Expiration queries
    Flux<UserRole> findByExpiresAtBefore(LocalDateTime dateTime);
    Flux<UserRole> findByUserIdAndExpiresAtAfter(UUID userId, LocalDateTime dateTime);

    // Grant/revoke operations
    @Modifying
    @Query("UPDATE user_roles SET is_active = false WHERE user_id = :userId AND role_id = :roleId")
    Mono<Integer> revokeUserRole(UUID userId, UUID roleId);

    @Modifying
    @Query("UPDATE user_roles SET is_active = true WHERE user_id = :userId AND role_id = :roleId")
    Mono<Integer> activateUserRole(UUID userId, UUID roleId);

    // Cleanup expired roles
    @Modifying
    @Query("UPDATE user_roles SET is_active = false WHERE expires_at < :now AND is_active = true")
    Mono<Integer> deactivateExpiredRoles(LocalDateTime now);
}