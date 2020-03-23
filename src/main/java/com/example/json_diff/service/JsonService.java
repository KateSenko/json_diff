package com.example.json_diff.service;

/**
 * The interface Json service.
 */
public interface JsonService {

    /**
     * Accepts encoded json, decodes it and saves in repository
     *
     * @param id          the json id
     * @param encodedJson the encoded json
     * @param side        side LEFT/RIGHT
     */
    void acceptEncodedJson(Long id, String encodedJson, Side side);
}
