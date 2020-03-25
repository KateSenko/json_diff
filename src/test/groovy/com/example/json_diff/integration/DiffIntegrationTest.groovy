package com.example.json_diff.integration

import groovy.json.JsonOutput
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification
import spock.lang.Stepwise

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Stepwise
class DiffIntegrationTest extends Specification {

    @Autowired
    private MockMvc mvc

    def "when left json is accepted response has status is 200'"() {
        given:
            def encodedJson = 'ewogInRlc3QiOiI1dmFsdWU2Igp9'
            def content = JsonOutput.toJson(data : encodedJson)

        expect:
            mvc.perform(
                    post("/v1/diff/1/left")
                            .contentType('application/json')
                            .accept('application/json')
                            .content(content)
            ).andExpect(status().isOk())
    }

    def "when right json is accepted response has status is 200'"() {
        given:
            def encodedJson = 'ewogInRlc3QiOiI1dmFsdWU2Igp9'
            def content = JsonOutput.toJson(data : encodedJson)

        expect:
            mvc.perform(
                    post("/v1/diff/1/right")
                            .contentType('application/json')
                            .accept('application/json')
                            .content(content)
            ).andExpect(status().isOk())
    }

    def "when diff returns success 200'"() {
        expect:
            mvc.perform(
                    get("/v1/diff/1")
                            .contentType('application/json')
                            .accept('application/json')
            ).andExpect(status().isOk())
    }

}
