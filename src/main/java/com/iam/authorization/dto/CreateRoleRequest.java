package com.iam.authorization.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoleRequest {

    @NotBlank(message = "Role name is required")
    private String roleName;

    private String description;

    @NotNull(message = "Organization ID is required")
    private Integer orgId;

    private Integer departmentId;

    private Boolean isSystemRole = false;

    private List<UUID> permissionIds;
}