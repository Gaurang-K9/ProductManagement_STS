package com.demo.security;

public final class SecurityConstants {

    private  SecurityConstants() {}

    public static final String[] JWT_EXCLUDED = {
            "/login",
            "/register",
            "/users/update/password"            //Included since if the token expires during password reset user may get locked out
    };

    public static final String[] FIRST_LOGIN_ALLOWED = {
            "/login",
            "/users/update/password"
    };
}
