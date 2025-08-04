package com.iam.authorization.config;

public final class ApiRoutes {

    private ApiRoutes() {} // Prevent instantiation

    public static final String API_V1 = "/api/v1";

    // Authorization base routes
    public static final String AUTHORIZATION = API_V1 + "/authorization";
    public static final String ROLES = API_V1 + "/roles";
    public static final String PERMISSIONS = API_V1 + "/permissions";

    // Role endpoints
    public static final String ROLE_BY_ID = "/{roleId}";
    public static final String ROLES_BY_ORGANIZATION = "/organization/{orgId}";
    public static final String ROLES_BY_DEPARTMENT = "/department/{departmentId}";

    // Permission endpoints
    public static final String PERMISSION_BY_ID = "/{permissionId}";
    public static final String PERMISSIONS_BY_RESOURCE = "/resource/{resource}";
    public static final String PERMISSIONS_BY_ROLE = "/role/{roleId}";

    // Authorization endpoints
    public static final String ASSIGN_ROLE = "/assign-role";
    public static final String REVOKE_ROLE = "/users/{userId}/roles/{roleId}";
    public static final String CHECK_PERMISSION = "/check-permission";
    public static final String USER_ROLES = "/users/{userId}/roles";
    public static final String USER_PERMISSIONS = "/users/{userId}/permissions";
    public static final String HEALTH = "/health";
}