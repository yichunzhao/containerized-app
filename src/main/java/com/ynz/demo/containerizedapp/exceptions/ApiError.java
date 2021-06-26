package com.ynz.demo.containerizedapp.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * status – the HTTP status code
 * message – the error message associated with exception
 * error – List of constructed error messages
 */

@Getter
public class ApiError {
    private HttpStatus status;
    private String message;
    private List<String> errors;
    private OffsetDateTime timeStamp;

    public ApiError(HttpStatus status, String message, List<String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.timeStamp = OffsetDateTime.now();
    }

    public ApiError(HttpStatus status, String message, String error) {
        this.status = status;
        this.message = message;
        this.errors = Arrays.asList(error);
        this.timeStamp = OffsetDateTime.now();
    }

}
