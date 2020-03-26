package com.example.json_diff.integration


import groovy.json.JsonOutput
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Shared
import spock.lang.Specification

import static org.hamcrest.Matchers.is
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class AcceptJsonIntegrationTest extends Specification {

    @Autowired
    private MockMvc mvc

    @Shared
    def encodedJson = 'ewogInRlc3QiOiI1dmFsdWU2Igp9'
    @Shared
    def content = JsonOutput.toJson(data: encodedJson)

    def "when left json is accepted response has status is 200'"() {
        expect:
            mvc.perform(
                    post("/v1/diff/123/left")
                            .contentType('application/json')
                            .accept('application/json')
                            .content(content))
                    .andExpect(status().isOk())
    }

    def "when right json is accepted response has status is 200'"() {
        expect:
            mvc.perform(
                    post("/v1/diff/123/right")
                            .contentType('application/json')
                            .accept('application/json')
                            .content(content))
                    .andExpect(status().isOk())
    }

    def "bad request returns if provided json can not be decoded"() {
        given:
            def notEncodedJson = '{invalid_json}'
            def content = JsonOutput.toJson(data: notEncodedJson)
        and:
            def expectedErrorMessage = "Json can't be decoded. Reason: Illegal base64 character 7b"
        expect:
            mvc.perform(
                    post("/v1/diff/123/right")
                            .contentType('application/json')
                            .accept('application/json')
                            .content(content))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath('$.errorMessage', is(expectedErrorMessage)))
    }


}
