package com.iam.authorization.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignRoleRequest {

    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotNull(message = "Role ID is required")
    private UUID roleId;

    private LocalDateTime expiresAt;
}