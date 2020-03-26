package com.example.json_diff.service;

import com.example.json_diff.exception.InvalidJsonException;
import com.example.json_diff.repository.JsonDiffRepository;
import com.example.json_diff.repository.entity.JsonDiffEntity;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class JsonServiceImpl implements JsonService {

    private final JsonDiffRepository jsonDiffRepository;

    public JsonServiceImpl(JsonDiffRepository jsonDiffRepository) {
        this.jsonDiffRepository = jsonDiffRepository;
    }

    @Override
    public void acceptEncodedJson(Long id, String encodedJson, Side side) {
        String decodedJson = decodeJson(encodedJson);

        JsonDiffEntity jsonDiffEntity = jsonDiffRepository.findById(id).orElse(new JsonDiffEntity());
        jsonDiffEntity.setId(id);

        if(side.equals(Side.LEFT)) {
            jsonDiffEntity.setLeftJson(decodedJson);
        } else {
            jsonDiffEntity.setRightJson(decodedJson);
        }

        jsonDiffRepository.save(jsonDiffEntity);
    }

    private String decodeJson(String encodedJson) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(encodedJson);
            return new String(decodedBytes);
        } catch (IllegalArgumentException ex) {
            throw new InvalidJsonException(ex.getMessage());
        }

    }
}
