package com.demo.exception;

public class UnauthorizedAccessException extends RuntimeException {

    public UnauthorizedAccessException(String message) {
        super(message);
    }

    public static UnauthorizedAccessException forResource(Class<?> resourceClass, Object resourceId) {
        return new UnauthorizedAccessException("You are not allowed to access this " + resourceClass.getSimpleName()
                        + " (id: " + resourceId + ")");
    }

    public static UnauthorizedAccessException forAction(String action, Class<?> resourceClass) {
        return new UnauthorizedAccessException("You are not allowed to " + action + " this " + resourceClass.getSimpleName());
    }
}
