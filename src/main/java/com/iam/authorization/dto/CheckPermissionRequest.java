package com.iam.authorization.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckPermissionRequest {

    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotBlank(message = "Resource is required")
    private String resource;

    @NotBlank(message = "Action is required")
    private String action;
}