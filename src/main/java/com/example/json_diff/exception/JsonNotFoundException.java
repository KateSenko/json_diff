package com.example.json_diff.exception;

public class JsonNotFoundException extends RuntimeException {

    public JsonNotFoundException(Long id) {
        super(buildMessage(id));
    }

    private static String buildMessage(Long id) {
        return String.format("Json entity with id: [%s] is not found", id);
    }
}
