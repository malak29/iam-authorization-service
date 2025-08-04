package com.iam.authorization.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoleRequest {

    private String roleName;
    private String description;
    private Integer orgId;
    private Integer departmentId;
    private Boolean isActive;
    private List<UUID> permissionIds;
}