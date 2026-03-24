package com.demo.model.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {

    private LocalDateTime timestamp;
    private String message;
    private String error;

    public static ExceptionResponse of(String message, String error){
        return new ExceptionResponse(LocalDateTime.now(), message, error);
    }
}
