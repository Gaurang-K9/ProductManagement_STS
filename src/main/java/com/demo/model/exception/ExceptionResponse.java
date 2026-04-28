package com.demo.model.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {

    private Instant timestamp;
    private String message;
    private String error;

    public static ExceptionResponse of(String message, String error){
        return new ExceptionResponse(Instant.now(), message, error);
    }
}
