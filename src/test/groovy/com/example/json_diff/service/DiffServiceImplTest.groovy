package com.example.json_diff.service

import com.example.json_diff.exception.JsonNotFullyPresented
import com.example.json_diff.exception.JsonNotFoundException
import com.example.json_diff.repository.JsonDiffRepository
import com.example.json_diff.repository.entity.JsonDiffEntity
import spock.lang.Specification

/**
 * Unit tests for {@link DiffServiceImpl}
 */
class DiffServiceImplTest extends Specification {

    def jsonDiffRepository = Mock(JsonDiffRepository)

    //system under tests
    def sut = new DiffServiceImpl(jsonDiffRepository)

    def "sut returns EQUALS in result for equals left and right json sides"() {
        given:
            def jsonId = 1L
            def leftJson = ' {"text" : "value"} '
            def rightJson = ' {"text" : "value"} '
            def expectedOffsets = null
            def jsonEntity = new JsonDiffEntity(id: jsonId, leftJson: leftJson, rightJson: rightJson)
        and:
            jsonDiffRepository.findById(jsonId) >> Optional.of(jsonEntity)

        when:
            def result = sut.getJsonDiff(jsonId)

        then:
            result.getType() == DiffType.EQUALS
            result.getOffsets() == expectedOffsets
    }

    def "sut returns different content result with offsets"() {
        given:
            def jsonId = 1L
            def leftJson = ' {"text" : "value1"} '
            def rightJson = ' {"test" : "value2"} '
            def expectedOffsets = '5, 17'
            def jsonEntity = new JsonDiffEntity(id: jsonId, leftJson: leftJson, rightJson: rightJson)
        and:
            jsonDiffRepository.findById(jsonId) >> Optional.of(jsonEntity)

        when:
            def result = sut.getJsonDiff(jsonId)

        then:
            result.getType() == DiffType.DIFFERENT_CONTENT
            result.getOffsets() == expectedOffsets
    }

    def "sut returns different length result for jsons with different length"() {
        given:
            def jsonId = 1L
            def leftJson = ' {"text" : "value"} '
            def rightJson = ' {"text" : "value_another_length"} '
            def expectedOffsets = null
            def jsonEntity = new JsonDiffEntity(id: jsonId, leftJson: leftJson, rightJson: rightJson)
        and:
            jsonDiffRepository.findById(jsonId) >> Optional.of(jsonEntity)

        when:
            def result = sut.getJsonDiff(jsonId)

        then:
            result.getType() == DiffType.DIFFERENT_LENGTH
            result.getOffsets() == expectedOffsets
    }

    def "sut throws exception if json with requested id does not exist"() {
        given:
            def jsonId = 1L
            def expectedErrorMessage = 'Json entity with id [1] is not found'
        and:
            jsonDiffRepository.findById(jsonId) >> Optional.empty()

        when:
            sut.getJsonDiff(jsonId)

        then:
            def exception = thrown(JsonNotFoundException)
            exception.message == expectedErrorMessage
    }

    def "sut throws exception if right json side is not presented"() {
        given:
            def jsonId = 1L
            def leftJson = ' {"text" : "value"} '
            def jsonEntity = new JsonDiffEntity(id: jsonId, leftJson: leftJson, rightJson: null)
        and:
            jsonDiffRepository.findById(jsonId) >> Optional.of(jsonEntity)
        and:
            def expectedErrorMessage = "Json sides are not fully presented"

        when:
            sut.getJsonDiff(jsonId)

        then:
            def exception = thrown(JsonNotFullyPresented)
            exception.message == expectedErrorMessage
    }

    def "sut throws exception if left json side is not presented"() {
        given:
            def jsonId = 1L
            def rightJson = ' {"text" : "value"} '
            def jsonEntity = new JsonDiffEntity(id: jsonId, rightJson: rightJson, leftJson: null)
        and:
            jsonDiffRepository.findById(jsonId) >> Optional.of(jsonEntity)
        and:
            def expectedErrorMessage = "Json sides are not fully presented"

        when:
            sut.getJsonDiff(jsonId)

        then:
            def exception = thrown(JsonNotFullyPresented)
            exception.message == expectedErrorMessage
    }
}
