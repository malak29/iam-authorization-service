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

@Table("roles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @Column("role_id")
    private UUID roleId;

    @Column("role_name")
    private String roleName;

    @Column("description")
    private String description;

    @Column("org_id")
    private Integer orgId;

    @Column("department_id")
    private Integer departmentId;

    @Column("is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column("is_system_role")
    @Builder.Default
    private Boolean isSystemRole = false;

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