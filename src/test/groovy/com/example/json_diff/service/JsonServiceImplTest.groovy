package com.example.json_diff.service

import com.example.json_diff.exception.InvalidJsonException
import com.example.json_diff.repository.JsonDiffRepository
import com.example.json_diff.repository.entity.JsonDiffEntity
import spock.lang.Specification

/**
 * Unit tests for {@link JsonServiceImpl}
 */
class JsonServiceImplTest extends Specification {

    def jsonDiffRepository = Mock(JsonDiffRepository)

    //system under tests
    def sut = new JsonServiceImpl(jsonDiffRepository)

    def "sut decodes and updates provided left json in repository"() {
        given:
            def jsonId = 1L
            def encodedLeftJson = 'eyAidGVzdCI6InZhbHVlIiB9'
            def decodedLeftJson = '{ "test":"value" }'
            def rightJson  = '{"some": "json"}'
            def jsonDiffEntityFormDB = new JsonDiffEntity(id: jsonId, leftJson: null, rightJson: rightJson)
        and:
            jsonDiffRepository.findById(jsonId) >> Optional.of(jsonDiffEntityFormDB)

        when:
            sut.acceptEncodedJson(jsonId, encodedLeftJson, Side.LEFT)

        then:
            1 * jsonDiffRepository.save({ it.id == jsonId && it.leftJson == decodedLeftJson && it.rightJson == rightJson})
    }

    def "sut decodes and updates provided right json in repository"() {
        given:
            def jsonId = 1L
            def encodedRightJson = 'eyAidGVzdCI6InZhbHVlIiB9'
            def decodedRightJson = '{ "test":"value" }'
            def leftJson  = '{"some": "json"}'
            def jsonDiffEntityFormDB = new JsonDiffEntity(id: jsonId, leftJson: leftJson, rightJson: null)
        and:
            jsonDiffRepository.findById(jsonId) >> Optional.of(jsonDiffEntityFormDB)

        when:
            sut.acceptEncodedJson(jsonId, encodedRightJson, Side.RIGHT)

        then:
            1 * jsonDiffRepository.save({ it.id == jsonId && it.rightJson == decodedRightJson && it.leftJson == leftJson})
    }

    def "sut throws exception if json can not be decoded"() {
        given:
            def jsonId = 1L
            def invalidEncodedJson = '{"invalid": "content"}'
        and:
            jsonDiffRepository.findById(jsonId) >> Optional.empty()

        when:
            sut.acceptEncodedJson(jsonId, invalidEncodedJson, Side.RIGHT)

        then:
            thrown(InvalidJsonException)
    }

    def "sut decodes and instantiates new entity of json if it does not exist"() {
        given:
            def jsonId = 1L
            def encodedRightJson = 'eyAidGVzdCI6InZhbHVlIiB9'
            def decodedRightJson = '{ "test":"value" }'
        and:
            jsonDiffRepository.findById(jsonId) >> Optional.empty()

        when:
            sut.acceptEncodedJson(jsonId, encodedRightJson, Side.RIGHT)

        then:
            1 * jsonDiffRepository.save({ it.id == jsonId && it.rightJson == decodedRightJson && it.leftJson == null})
    }
}
