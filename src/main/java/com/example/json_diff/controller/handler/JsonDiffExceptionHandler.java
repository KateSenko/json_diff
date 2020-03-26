package com.example.json_diff.controller.handler;

import com.example.json_diff.exception.InvalidJsonException;
import com.example.json_diff.exception.JsonNotFoundException;
import com.example.json_diff.exception.JsonNotFullyPresented;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = "com.example.json_diff.controller")
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class JsonDiffExceptionHandler {
    private static final String ERROR_MESSAGE_PATTERN = "Error: {}";

    @ExceptionHandler(value = JsonNotFullyPresented.class)
    public ResponseEntity<Object> handleJsonNotFullyPresented(JsonNotFullyPresented exception) {
        log.error(ERROR_MESSAGE_PATTERN, exception.getMessage());
        return createErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = JsonNotFoundException.class)
    public ResponseEntity<Object> handleJsonNotFoundException(JsonNotFoundException exception) {
        log.error(ERROR_MESSAGE_PATTERN, exception.getMessage());
        return createErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InvalidJsonException.class)
    public ResponseEntity<Object> handleInvalidJsonException(InvalidJsonException exception) {
        log.error(ERROR_MESSAGE_PATTERN, exception.getMessage());
        return createErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> createErrorResponse(String errorMessage, HttpStatus status) {
        RestErrorMessage restErrorMessage = new RestErrorMessage(errorMessage);
        return new ResponseEntity<>(restErrorMessage, status);
    }
}
