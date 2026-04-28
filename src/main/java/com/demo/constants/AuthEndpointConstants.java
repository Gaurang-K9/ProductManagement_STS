package com.demo.constants;

public final class AuthEndpointConstants {

    private AuthEndpointConstants() {}

    public static final String[] JWT_EXCLUDED = {
            "/auth/login",
            "/auth/register",
            "/auth/refresh"
    };

    public static final String[] FIRST_LOGIN_ALLOWED = {
            "/auth/login",
            "/users/update/password"
    };
}
