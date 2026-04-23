package com.demo.shared;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private Instant timestamp;
    private String message;
    private T data;

    public static <T> ApiResponse<T> of(String message, T data) {
        return new ApiResponse<>(Instant.now(), message, data);
    }

    public static <T> ApiResponse<T> of(String message) {
        return new ApiResponse<>(Instant.now(), message, null);
    }

    public static <T> ApiResponse<T> of(T data) {
        return new ApiResponse<>(Instant.now(), null, data);
    }
}
