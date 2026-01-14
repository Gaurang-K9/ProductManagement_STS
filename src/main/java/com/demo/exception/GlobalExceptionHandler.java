package com.demo.exception;

import com.demo.model.exception.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleResourceNotFoundException(ResourceNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).
                body(ExceptionResponse.of("Resource Not Found", exception.getMessage()));
    }

    @ExceptionHandler(ConflictResourceException.class)
    public ResponseEntity<ExceptionResponse> handleConflictResourceException(ConflictResourceException exception){
        return ResponseEntity.status(HttpStatus.CONFLICT).
                body(ExceptionResponse.of("Conflict Occured For Given Resource", exception.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentials(BadCredentialsException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).
                body(ExceptionResponse.of("Bad Credentials", exception.getMessage()));
    }

    @ExceptionHandler(ForbiddenAccessException.class)
    public ResponseEntity<ExceptionResponse> handleAuthorizedAccess(ForbiddenAccessException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).
                body(ExceptionResponse.of("Forbidden/ Access Denied", exception.getMessage()));
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ExceptionResponse> handleBadPassword(InvalidPasswordException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                body(ExceptionResponse.of("Password Validation Failed", exception.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequest(BadRequestException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                body(ExceptionResponse.of("Bad Request", exception.getMessage()));
    }
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception exception){
//        Map<String, Object> response = new HashMap<>();
//        response.put("timestamp", LocalDateTime.now());
//        response.put("message", "Internal Server Error");
//        response.put("error", exception.getMessage());
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//    }
}
