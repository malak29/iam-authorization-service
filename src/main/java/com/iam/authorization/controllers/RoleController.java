package com.iam.authorization.controller;

import com.iam.authorization.config.ApiRoutes;
import com.iam.authorization.dto.*;
import com.iam.authorization.service.RoleService;
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
@RequestMapping(ApiRoutes.ROLES)
@RequiredArgsConstructor
@Slf4j
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public Mono<ResponseEntity<ApiResponse<RoleResponse>>> createRole(@Valid @RequestBody Mono<CreateRoleRequest> requestMono) {
        return requestMono
                .doOnNext(request -> log.info("Creating role: {}", request.getRoleName()))
                .flatMap(roleService::createRole)
                .map(response -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(ApiResponse.success(response, "Role created successfully")))
                .doOnSuccess(response -> log.info("Role creation completed"));
    }

    @GetMapping(ApiRoutes.ROLE_BY_ID)
    public Mono<ResponseEntity<ApiResponse<RoleResponse>>> getRoleById(@PathVariable UUID roleId) {
        return roleService.getRoleById(roleId)
                .map(response -> ResponseEntity.ok(ApiResponse.success(response, "Role retrieved successfully")));
    }

    @GetMapping
    public Mono<ResponseEntity<ApiResponse<List<RoleResponse>>>> getAllRoles() {
        return roleService.getAllRoles()
                .collectList()
                .map(roles -> ResponseEntity.ok(ApiResponse.success(roles, "Roles retrieved successfully")));
    }

    @GetMapping(ApiRoutes.ROLES_BY_ORGANIZATION)
    public Mono<ResponseEntity<ApiResponse<List<RoleResponse>>>> getRolesByOrganization(@PathVariable Integer orgId) {
        return roleService.getRolesByOrganization(orgId)
                .collectList()
                .map(roles -> ResponseEntity.ok(ApiResponse.success(roles, "Organization roles retrieved successfully")));
    }

    @PutMapping(ApiRoutes.ROLE_BY_ID)
    public Mono<ResponseEntity<ApiResponse<RoleResponse>>> updateRole(
            @PathVariable UUID roleId,
            @Valid @RequestBody Mono<UpdateRoleRequest> requestMono) {

        return requestMono
                .flatMap(request -> roleService.updateRole(roleId, request))
                .map(response -> ResponseEntity.ok(ApiResponse.success(response, "Role updated successfully")));
    }

    @DeleteMapping(ApiRoutes.ROLE_BY_ID)
    public Mono<ResponseEntity<ApiResponse<Void>>> deleteRole(@PathVariable UUID roleId) {
        return roleService.deleteRole(roleId)
                .then(Mono.just(ResponseEntity.ok(ApiResponse.<Void>success("Role deleted successfully"))));
    }
}