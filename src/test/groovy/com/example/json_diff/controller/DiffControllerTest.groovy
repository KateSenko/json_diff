package com.example.json_diff.controller

import com.example.json_diff.service.DiffService
import com.example.json_diff.service.JsonService
import com.example.json_diff.service.Side
import spock.lang.Shared
import spock.lang.Specification

/**
 * Unit tests for {@link DiffController}
 */
class DiffControllerTest extends Specification {

    def jsonService = Mock(JsonService)
    def diffService = Mock(DiffService)

    //system under test
    def sut = new DiffController(jsonService, diffService)

    @Shared def jsonId = 1L
    @Shared def encodedJson = 'encoded_json'
    @Shared def encodedRequest = Mock(EncodedJsonRequest) {
        getData() >> encodedJson
    }

    def "sut uses json service to accept left json"() {
        when:
            sut.acceptLeft(jsonId, encodedRequest)
        then:
            1 * jsonService.acceptEncodedJson(jsonId, encodedJson, Side.LEFT)
    }

    def "sut uses json service to accept right json"() {
        when:
            sut.acceptRight(jsonId, encodedRequest)
        then:
            1 * jsonService.acceptEncodedJson(jsonId, encodedJson, Side.RIGHT)
    }

    def "sut uses diff service to get json diff"() {
        when:
            sut.getDiff(jsonId)
        then:
            1 * diffService.getJsonDiff(jsonId)
    }
}
