package com.iam.authorization.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePermissionRequest {

    @NotBlank(message = "Permission name is required")
    private String permissionName;

    @NotBlank(message = "Resource is required")
    private String resource;

    @NotBlank(message = "Action is required")
    private String action;

    private String description;
    private Boolean isSystemPermission = false;
}