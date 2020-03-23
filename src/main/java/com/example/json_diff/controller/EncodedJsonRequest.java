package com.example.json_diff.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
class EncodedJsonRequest {
    @NotBlank
    String data;
}
