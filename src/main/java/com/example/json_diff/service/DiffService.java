package com.example.json_diff.service;


/**
 * The interface Diff service.
 */
public interface DiffService {

    /**
     * Compares left and right sides of provided json by ID
     * Returns difference type from the list:
     * EQUALS, DIFFERENT_LENGTH, DIFFERENT_CONTENT
     *
     * If type is DIFFERENT_CONTENT, that the result also contains offsets
     * where differences where found
     *
     * @param id the json id
     * @return the json diff
     */
    DiffResult getJsonDiff(Long id);

}
