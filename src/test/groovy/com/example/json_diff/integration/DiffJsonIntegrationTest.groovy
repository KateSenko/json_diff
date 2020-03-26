package com.example.json_diff.integration

import com.example.json_diff.repository.JsonDiffRepository
import com.example.json_diff.repository.entity.JsonDiffEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.hamcrest.Matchers.blankOrNullString
import static org.hamcrest.Matchers.is
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class DiffJsonIntegrationTest extends Specification {

    @Autowired
    private MockMvc mvc
    @Autowired
    private JsonDiffRepository repository

    def "diff returns EQUAL if there is not diff between left and right sides"() {
        given:
            def jsonId = 1
            def jsonContent = '{"some": "json"}'
            def equalJsonEntity = new JsonDiffEntity(id: jsonId, leftJson: jsonContent, rightJson: jsonContent)
        and:
            repository.save(equalJsonEntity)
        and:
            def expectedType = 'EQUALS'

        expect:
            mvc.perform(
                    get("/v1/diff/$jsonId")
                            .contentType('application/json')
                            .accept('application/json'))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.type', is(expectedType)))
                    .andExpect(jsonPath('$.offsets', blankOrNullString()))
    }

    def "diff returns DIFFERENT_CONTENT if left and right side has different content'"() {
        given:
            def jsonId = 2
            def jsonLeftContent = '{"some": "json1"}'
            def jsonRightContent = '{"some": "json2"}'
            def equalJsonEntity = new JsonDiffEntity(id: jsonId, leftJson: jsonLeftContent, rightJson: jsonRightContent)
        and:
            repository.save(equalJsonEntity)
        and:
            def expectedType = 'DIFFERENT_CONTENT'
            def expectedOffsets = '14; length: 17'

        expect:
            mvc.perform(
                    get("/v1/diff/$jsonId")
                            .contentType('application/json')
                            .accept('application/json'))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.type', is(expectedType)))
                    .andExpect(jsonPath('$.offsets', is(expectedOffsets)))
    }

    def "diff returns DIFFERENT_LENGTH if left and right side has different length"() {
        given:
            def jsonId = 3
            def jsonLeftContent = '{"some": "json"}'
            def jsonRightContent = '{"some": "json_another_length"}'
            def equalJsonEntity = new JsonDiffEntity(id: jsonId, leftJson: jsonLeftContent, rightJson: jsonRightContent)
        and:
            repository.save(equalJsonEntity)
        and:
            def expectedType = 'DIFFERENT_LENGTH'

        expect:
            mvc.perform(
                    get("/v1/diff/$jsonId")
                            .contentType('application/json')
                            .accept('application/json'))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath('$.type', is(expectedType)))
                    .andExpect(jsonPath('$.offsets', is(blankOrNullString())))
    }

    def "bad request returns on diff request if json don't has both sides stored in repository"() {
        given:
            def jsonId = 4
            def jsonLeftContent = '{"some": "json"}'
            def jsonRightContent = null
            def equalJsonEntity = new JsonDiffEntity(id: jsonId, leftJson: jsonLeftContent, rightJson: jsonRightContent)
        and:
            repository.save(equalJsonEntity)
        and:
            def expectedErrorMessage = 'Json sides are not fully presented'

        expect:
            mvc.perform(
                    get("/v1/diff/$jsonId")
                            .contentType('application/json')
                            .accept('application/json'))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath('$.errorMessage', is(expectedErrorMessage)))
    }

    def "bad request returns if json with requested id is not found"() {
        given:
            def jsonId = 1000
        and:
            def expectedErrorMessage = 'Json entity with id [1000] is not found'

        expect:
            mvc.perform(
                    get("/v1/diff/$jsonId")
                            .contentType('application/json')
                            .accept('application/json'))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath('$.errorMessage', is(expectedErrorMessage)))
    }

}
