package com.demo.constants;

public final class AuthEndpointConstants {

    private AuthEndpointConstants() {}

    public static final String[] JWT_EXCLUDED = {
            "/login",
            "/register",
    };

    public static final String[] FIRST_LOGIN_ALLOWED = {
            "/login",
            "/users/update/password"
    };
}
