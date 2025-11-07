package com.demo.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Class<?> resourceClass, String identifierName, Object identifierValue) {
        super(buildMessage(resourceClass, identifierName, identifierValue));
    }

    private static String buildMessage(Class<?> resourceClass, String identifierName, Object identifierValue) {
        return "Could Not Locate Resource "
                + resourceClass.getSimpleName()
                + ": " + identifierName + " " + identifierValue;
    }
}
