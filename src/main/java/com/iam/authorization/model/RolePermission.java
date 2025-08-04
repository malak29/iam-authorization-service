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

@Table("role_permissions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolePermission {

    @Id
    @Column("role_permission_id")
    private UUID rolePermissionId;

    @Column("role_id")
    private UUID roleId;

    @Column("permission_id")
    private UUID permissionId;

    @Column("granted_by")
    private UUID grantedBy;

    @Column("is_active")
    @Builder.Default
    private Boolean isActive = true;

    @CreatedDate
    @Column("granted_at")
    private LocalDateTime grantedAt;
}