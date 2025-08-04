package com.iam.authorization.dto;

import com.iam.authorization.model.Permission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PermissionResponse {

    private UUID permissionId;
    private String permissionName;
    private String resource;
    private String action;
    private String description;
    private Boolean isActive;
    private Boolean isSystemPermission;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID createdBy;
    private UUID updatedBy;

    public PermissionResponse(Permission permission) {
        this.permissionId = permission.getPermissionId();
        this.permissionName = permission.getPermissionName();
        this.resource = permission.getResource();
        this.action = permission.getAction();
        this.description = permission.getDescription();
        this.isActive = permission.getIsActive();
        this.isSystemPermission = permission.getIsSystemPermission();
        this.createdAt = permission.getCreatedAt();
        this.updatedAt = permission.getUpdatedAt();
        this.createdBy = permission.getCreatedBy();
        this.updatedBy = permission.getUpdatedBy();
    }
}