package com.example.json_diff.service;

import com.example.json_diff.exception.DiffException;
import com.example.json_diff.exception.JsonNotFoundException;
import com.example.json_diff.repository.JsonDiffRepository;
import com.example.json_diff.repository.entity.JsonDiffEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DiffServiceImpl implements DiffService {

    @Autowired
    private JsonDiffRepository jsonDiffRepository;

    @Override
    public DiffResult getJsonDiff(Long id) {
        JsonDiffEntity jsonDiffEntity = jsonDiffRepository.findById(id).orElseThrow(() -> new JsonNotFoundException(id));
        return compareJsonSides(jsonDiffEntity);
    }

    private DiffResult compareJsonSides(JsonDiffEntity jsonDiffEntity) {
        if (StringUtils.isBlank(jsonDiffEntity.getLeftJson()) || StringUtils.isBlank(jsonDiffEntity.getRightJson())) {
            throw new DiffException("Json sides are not fully presented");
        }

        char[] left = jsonDiffEntity.getLeftJson().toCharArray();
        char[] right = jsonDiffEntity.getRightJson().toCharArray();

        if (Arrays.equals(left, right)) {
            return DiffResult.builder().type(DiffType.EQUALS).build();
        }

        if (left.length != right.length) {
            return DiffResult.builder().type(DiffType.DIFFERENT_LENGTH).build();
        }

        List<String> offsets = new ArrayList<>();

        for (int i = 0; i < left.length; i++) {
            if (left[i] != right[i]) {
                offsets.add(String.valueOf(i));
            }
        }

        final String message = "Offsets: " + String.join(",", offsets.toArray(new String[0])) + " - Length: " + left.length;

        return DiffResult.builder()
                .type(DiffType.DIFFERENT_CONTENT)
                .offsets(message)
                .build();
    }
}
