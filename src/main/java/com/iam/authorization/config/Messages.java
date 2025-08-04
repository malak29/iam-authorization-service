package com.iam.authorization.config;

public final class Messages {

    private Messages() {}

    // Success messages
    public static final String ROLE_CREATED_SUCCESS = "Role created successfully";
    public static final String ROLE_UPDATED_SUCCESS = "Role updated successfully";
    public static final String ROLE_DELETED_SUCCESS = "Role deleted successfully";
    public static final String ROLE_RETRIEVED_SUCCESS = "Role retrieved successfully";
    public static final String ROLES_RETRIEVED_SUCCESS = "Roles retrieved successfully";

    public static final String PERMISSION_CREATED_SUCCESS = "Permission created successfully";
    public static final String PERMISSION_UPDATED_SUCCESS = "Permission updated successfully";
    public static final String PERMISSION_DELETED_SUCCESS = "Permission deleted successfully";
    public static final String PERMISSION_RETRIEVED_SUCCESS = "Permission retrieved successfully";
    public static final String PERMISSIONS_RETRIEVED_SUCCESS = "Permissions retrieved successfully";

    public static final String ROLE_ASSIGNED_SUCCESS = "Role assigned successfully";
    public static final String ROLE_REVOKED_SUCCESS = "Role revoked successfully";
    public static final String PERMISSION_CHECK_SUCCESS = "Permission check completed";

    // Error messages
    public static final String ROLE_NOT_FOUND = "Role not found with ID: %s";
    public static final String PERMISSION_NOT_FOUND = "Permission not found with ID: %s";
    public static final String ROLE_NAME_EXISTS = "Role with name '%s' already exists";
    public static final String PERMISSION_NAME_EXISTS = "Permission with name '%s' already exists";
    public static final String USER_ROLE_EXISTS = "User already has this role";
    public static final String ROLE_PERMISSION_EXISTS = "Role already has this permission";
    public static final String INTERNAL_SERVER_ERROR = "An unexpected error occurred while processing your request";

    // Validation messages
    public static final String INVALID_ROLE_ID = "Invalid role ID format";
    public static final String INVALID_PERMISSION_ID = "Invalid permission ID format";
    public static final String INVALID_USER_ID = "Invalid user ID format";
}