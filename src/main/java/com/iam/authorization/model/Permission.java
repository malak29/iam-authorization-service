package com.iam.authorization.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Table("permissions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission {

    @Id
    @Column("permission_id")
    private UUID permissionId;

    @Column("permission_name")
    private String permissionName;

    @Column("resource")
    private String resource;

    @Column("action")
    private String action;

    @Column("description")
    private String description;

    @Column("is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column("is_system_permission")
    @Builder.Default
    private Boolean isSystemPermission = false;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private LocalDateTime updatedAt;

    @Column("created_by")
    private UUID createdBy;

    @Column("updated_by")
    private UUID updatedBy;
}