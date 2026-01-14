package com.demo.exception;

public class ForbiddenAccessException extends RuntimeException {

    public ForbiddenAccessException(String message) {
        super(message);
    }

    public static ForbiddenAccessException forResource(Class<?> resourceClass, Object resourceId) {
        return new ForbiddenAccessException("You are not allowed to access this " + resourceClass.getSimpleName()
                        + " (id: " + resourceId + ")");
    }

    public static ForbiddenAccessException forAction(String action, Class<?> resourceClass) {
        return new ForbiddenAccessException("You are not allowed to " + action + " this " + resourceClass.getSimpleName());
    }
}
