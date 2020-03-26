package com.example.json_diff.service;

import com.example.json_diff.exception.JsonNotFullyPresented;
import com.example.json_diff.exception.JsonNotFoundException;
import com.example.json_diff.repository.JsonDiffRepository;
import com.example.json_diff.repository.entity.JsonDiffEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DiffServiceImpl implements DiffService {

    private final JsonDiffRepository jsonDiffRepository;

    public DiffServiceImpl(JsonDiffRepository jsonDiffRepository) {
        this.jsonDiffRepository = jsonDiffRepository;
    }

    @Override
    public DiffResult getJsonDiff(Long id) {
        JsonDiffEntity jsonDiffEntity = jsonDiffRepository.findById(id).orElseThrow(() -> new JsonNotFoundException(id));
        return compareJsonSides(jsonDiffEntity);
    }

    private DiffResult compareJsonSides(JsonDiffEntity jsonDiffEntity) {
        if (StringUtils.isBlank(jsonDiffEntity.getLeftJson()) || StringUtils.isBlank(jsonDiffEntity.getRightJson())) {
            throw new JsonNotFullyPresented();
        }

        char[] leftCharArray = jsonDiffEntity.getLeftJson().toCharArray();
        char[] rightCharArray = jsonDiffEntity.getRightJson().toCharArray();

        if (leftCharArray.length != rightCharArray.length) {
            return DiffResult.builder().type(DiffType.DIFFERENT_LENGTH).build();
        }
        if (Arrays.equals(leftCharArray, rightCharArray)) {
            return DiffResult.builder().type(DiffType.EQUALS).build();
        }

        List<String> offsets = new ArrayList<>();

        for (int i = 0; i < leftCharArray.length; i++) {
            if (leftCharArray[i] != rightCharArray[i]) {
                offsets.add(String.valueOf(i));
            }
        }

        String message = String.join(", ", offsets.toArray(new String[0])) + "; length: " + leftCharArray.length;

        return DiffResult.builder()
                .type(DiffType.DIFFERENT_CONTENT)
                .offsets(message)
                .build();
    }
}
