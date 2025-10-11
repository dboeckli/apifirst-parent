package ch.guru.springframework.apifirst.apifirstserver.server.health;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class ActuatorInfoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    BuildProperties buildProperties;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Test
    void actuatorInfoTest() throws Exception {
        mockMvc.perform(get("/actuator/info"))
            .andDo(result -> log.info("Response (pretty):\n{}", pretty(result.getResponse().getContentAsString())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.git.commit.id.abbrev").isString())
            .andExpect(jsonPath("$.build.artifact").value(buildProperties.getArtifact()))
            .andExpect(jsonPath("$.build.group").value(buildProperties.getGroup()))
            .andExpect(jsonPath("$.java.version").value(startsWith("21")));
    }

    @Test
    void actuatorHealthTest() throws Exception {
        mockMvc.perform(get("/actuator/health/readiness"))
            .andDo(result -> log.info("Response (pretty):\n{}", pretty(result.getResponse().getContentAsString())))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("UP"))
            .andReturn();
    }

    private String pretty(String body) {
        try {
            Object json = OBJECT_MAPPER.readValue(body, Object.class);
            return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        } catch (Exception e) {
            // Falls kein valides JSON: unverändert zurückgeben
            return body;
        }
    }
}