package com.example.json_diff.service;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DiffResult {
    private DiffType type;
    private String offsets;
}
