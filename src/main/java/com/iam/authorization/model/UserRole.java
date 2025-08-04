package com.iam.authorization.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("user_roles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {

    @Id
    @Column("user_role_id")
    private UUID userRoleId;

    @Column("user_id")
    private UUID userId;

    @Column("role_id")
    private UUID roleId;

    @Column("granted_by")
    private UUID grantedBy;

    @Column("is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column("expires_at")
    private LocalDateTime expiresAt;

    @CreatedDate
    @Column("granted_at")
    private LocalDateTime grantedAt;
}