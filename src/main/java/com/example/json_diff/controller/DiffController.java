package com.example.json_diff.controller;

import com.example.json_diff.service.DiffResult;
import com.example.json_diff.service.DiffService;
import com.example.json_diff.service.JsonService;
import com.example.json_diff.service.Side;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/v1/diff/")
public class DiffController {

    private final JsonService jsonService;
    private final DiffService diffService;

    public DiffController(JsonService jsonService, DiffService diffService) {
        this.jsonService = jsonService;
        this.diffService = diffService;
    }

    @PostMapping(value = "{id}/left")
    public void acceptLeft(@PathVariable("id") Long jsonId,
                           @RequestBody @Valid EncodedJsonRequest request) {
        jsonService.acceptEncodedJson(jsonId, request.getData(), Side.LEFT);
    }

    @PostMapping(value = "{id}/right")
    public void acceptRight(@PathVariable("id") Long jsonId,
                            @RequestBody @Valid EncodedJsonRequest request) {
        jsonService.acceptEncodedJson(jsonId, request.getData(), Side.RIGHT);
    }

    @GetMapping(value = "{id}")
    public HttpEntity<DiffResult> getDiff(@PathVariable("id") Long jsonId) {
        return ResponseEntity.ok(diffService.getJsonDiff(jsonId));
    }
}
