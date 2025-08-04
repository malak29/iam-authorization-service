package com.iam.authorization.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionCheckResponse {

    private UUID userId;
    private String resource;
    private String action;
    private Boolean hasPermission;
    private List<String> grantingRoles;
    private String reason;
}