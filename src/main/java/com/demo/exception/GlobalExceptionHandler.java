package com.demo.exception;

import com.demo.model.exception.ExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleResourceNotFoundException(ResourceNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ExceptionResponse.of("Resource Not Found", exception.getMessage()));
    }

    @ExceptionHandler(ConflictResourceException.class)
    public ResponseEntity<ExceptionResponse> handleConflictResourceException(ConflictResourceException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(ExceptionResponse.of("Conflict Occured For Given Resource", exception.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentials(BadCredentialsException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ExceptionResponse.of("Bad Credentials", exception.getMessage()));
    }

    @ExceptionHandler(ForbiddenAccessException.class)
    public ResponseEntity<ExceptionResponse> handleAuthorizedAccess(ForbiddenAccessException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(ExceptionResponse.of("Forbidden/ Access Denied", exception.getMessage()));
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ExceptionResponse> handleBadPassword(InvalidPasswordException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ExceptionResponse.of("Password Validation Failed", exception.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequest(BadRequestException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ExceptionResponse.of("Bad Request", exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldError().getDefaultMessage();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ExceptionResponse.of("Validation Failed", message));
    }

    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    public ResponseEntity<ExceptionResponse> handleNotFound(Exception exception, HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponse.of("Resource Not Found", "Endpoint does not exist: " + request.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> handleTypeMismatch(MethodArgumentTypeMismatchException exception) {

        String paramName = exception.getName();
        String value = exception.getValue() != null ? exception.getValue().toString() : "null";

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.of("Invalid Parameter", "Invalid value '" + value + "' for parameter '" + paramName + "'"));
    }
}
