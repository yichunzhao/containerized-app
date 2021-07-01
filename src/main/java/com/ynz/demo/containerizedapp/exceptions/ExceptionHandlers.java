package com.ynz.demo.containerizedapp.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@ControllerAdvice
@Slf4j
public class ExceptionHandlers extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<ApiError> handleClientNotFoundException(ClientNotFoundException e) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, e.getMessage(), "Client not found error");
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicatedClientException.class)
    public ResponseEntity<ApiError> handleDuplicatedClientException(DuplicatedClientException e) {
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, e.getMessage(), "Duplicated Client error");
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BindingResult result = e.getBindingResult();

        List<String> errors = new ArrayList<>();

        List<String> fieldErrors = result.getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + "=> " + fieldError.getDefaultMessage())
                .collect(toList());

        List<String> globalErrors = result.getGlobalErrors().stream()
                .map(objectError -> objectError.getObjectName() + "=> " + objectError.getDefaultMessage())
                .collect(toList());

        errors.addAll(fieldErrors);
        errors.addAll(globalErrors);

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, e.getMessage(), errors);

        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException e) {

        List<String> errors = e.getConstraintViolations().stream()
                .map(constraintViolation -> constraintViolation.getRootBeanClass().getName()
                        + " " + constraintViolation.getPropertyPath() + " : " + constraintViolation.getMessage())
                .collect(toList());
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, e.getMessage(), errors);
        return ResponseEntity.badRequest().body(apiError);
    }

}
