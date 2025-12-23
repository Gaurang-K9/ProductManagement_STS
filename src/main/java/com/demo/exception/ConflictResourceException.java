package com.demo.exception;

public class ConflictResourceException extends RuntimeException {
    public ConflictResourceException(Class<?> resourceClass, String identifierName, Object identifierValue) {
        super(buildMessage(resourceClass, identifierName, identifierValue));
    }

    private static String buildMessage(Class<?> resourceClass, String identifierName, Object identifierValue) {
        return "Conflict Occurred For Resource "
                + resourceClass.getSimpleName()
                + ": " + identifierName + " " + identifierValue;
    }
}
