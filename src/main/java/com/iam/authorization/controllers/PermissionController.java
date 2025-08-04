package com.iam.authorization.controller;

import com.iam.authorization.config.ApiRoutes;
import com.iam.authorization.dto.*;
import com.iam.authorization.service.PermissionService;
import com.iam.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(ApiRoutes.PERMISSIONS)
@RequiredArgsConstructor
@Slf4j
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping
    public Mono<ResponseEntity<ApiResponse<PermissionResponse>>> createPermission(@Valid @RequestBody Mono<CreatePermissionRequest> requestMono) {
        return requestMono
                .doOnNext(request -> log.info("Creating permission: {}", request.getPermissionName()))
                .flatMap(permissionService::createPermission)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(ApiResponse.success(response, "Permission created successfully")));
    }

    @GetMapping(ApiRoutes.PERMISSION_BY_ID)
    public Mono<ResponseEntity<ApiResponse<PermissionResponse>>> getPermissionById(@PathVariable UUID permissionId) {
        return permissionService.getPermissionById(permissionId)
                .map(response -> ResponseEntity.ok(ApiResponse.success(response, "Permission retrieved successfully")));
    }

    @GetMapping
    public Mono<ResponseEntity<ApiResponse<List<PermissionResponse>>>> getAllPermissions() {
        return permissionService.getAllPermissions()
                .collectList()
                .map(permissions -> ResponseEntity.ok(ApiResponse.success(permissions, "Permissions retrieved successfully")));
    }

    @GetMapping(ApiRoutes.PERMISSIONS_BY_RESOURCE)
    public Mono<ResponseEntity<ApiResponse<List<PermissionResponse>>>> getPermissionsByResource(@PathVariable String resource) {
        return permissionService.getPermissionsByResource(resource)
                .collectList()
                .map(permissions -> ResponseEntity.ok(ApiResponse.success(permissions, "Resource permissions retrieved successfully")));
    }

    @DeleteMapping(ApiRoutes.PERMISSION_BY_ID)
    public Mono<ResponseEntity<ApiResponse<Void>>> deletePermission(@PathVariable UUID permissionId) {
        return permissionService.deletePermission(permissionId)
                .then(Mono.just(ResponseEntity.ok(ApiResponse.<Void>success("Permission deleted successfully"))));
    }
}