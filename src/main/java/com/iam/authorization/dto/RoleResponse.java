package com.iam.authorization.dto;

import com.iam.authorization.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse {

    private UUID roleId;
    private String roleName;
    private String description;
    private Integer orgId;
    private Integer departmentId;
    private Boolean isActive;
    private Boolean isSystemRole;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID createdBy;
    private UUID updatedBy;
    private List<PermissionResponse> permissions;
    private Integer userCount;

    public RoleResponse(Role role) {
        this.roleId = role.getRoleId();
        this.roleName = role.getRoleName();
        this.description = role.getDescription();
        this.orgId = role.getOrgId();
        this.departmentId = role.getDepartmentId();
        this.isActive = role.getIsActive();
        this.isSystemRole = role.getIsSystemRole();
        this.createdAt = role.getCreatedAt();
        this.updatedAt = role.getUpdatedAt();
        this.createdBy = role.getCreatedBy();
        this.updatedBy = role.getUpdatedBy();
    }
}