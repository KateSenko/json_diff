package com.example.json_diff.exception;

public class InvalidJsonException extends RuntimeException {
    private static String errorMessage = "Json can't be decoded. Reason: ";

    public InvalidJsonException(String reason) {
        super(errorMessage + reason);
    }
}
