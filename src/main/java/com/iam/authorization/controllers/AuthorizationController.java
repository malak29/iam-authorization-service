package com.iam.authorization.controller;

import com.iam.authorization.config.ApiRoutes;
import com.iam.authorization.dto.*;
import com.iam.authorization.service.AuthorizationService;
import com.iam.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(ApiRoutes.AUTHORIZATION)
@RequiredArgsConstructor
@Slf4j
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    @PostMapping(ApiRoutes.ASSIGN_ROLE)
    public Mono<ResponseEntity<ApiResponse<Void>>> assignRole(@Valid @RequestBody Mono<AssignRoleRequest> requestMono) {
        return requestMono
                .flatMap(authorizationService::assignRoleToUser)
                .then(Mono.just(ResponseEntity.ok(ApiResponse.<Void>success("Role assigned successfully"))));
    }

    @DeleteMapping(ApiRoutes.REVOKE_ROLE)
    public Mono<ResponseEntity<ApiResponse<Void>>> revokeRole(@PathVariable UUID userId, @PathVariable UUID roleId) {
        return authorizationService.revokeRoleFromUser(userId, roleId)
                .then(Mono.just(ResponseEntity.ok(ApiResponse.<Void>success("Role revoked successfully"))));
    }

    @PostMapping(ApiRoutes.CHECK_PERMISSION)
    public Mono<ResponseEntity<ApiResponse<PermissionCheckResponse>>> checkPermission(@Valid @RequestBody Mono<CheckPermissionRequest> requestMono) {
        return requestMono
                .flatMap(authorizationService::checkUserPermission)
                .map(response -> ResponseEntity.ok(ApiResponse.success(response, "Permission check completed")));
    }

    @GetMapping(ApiRoutes.USER_ROLES)
    public Mono<ResponseEntity<ApiResponse<List<RoleResponse>>>> getUserRoles(@PathVariable UUID userId) {
        return authorizationService.getUserRoles(userId)
                .collectList()
                .map(roles -> ResponseEntity.ok(ApiResponse.success(roles, "User roles retrieved successfully")));
    }

    @GetMapping(ApiRoutes.USER_PERMISSIONS)
    public Mono<ResponseEntity<ApiResponse<List<PermissionResponse>>>> getUserPermissions(@PathVariable UUID userId) {
        return authorizationService.getUserPermissions(userId)
                .collectList()
                .map(permissions -> ResponseEntity.ok(ApiResponse.success(permissions, "User permissions retrieved successfully")));
    }

    @GetMapping(ApiRoutes.HEALTH)
    public Mono<ResponseEntity<ApiResponse<String>>> health() {
        return Mono.just(ResponseEntity.ok(ApiResponse.success("OK", "Authorization service is running")));
    }
}