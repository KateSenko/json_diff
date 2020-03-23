package com.example.json_diff.exception;

public class JsonNotFullyPresented extends RuntimeException {

    private static String errorMessage = "Json sides are not fully presented";

    public JsonNotFullyPresented() {
        super(errorMessage);
    }
}
