package ch.guru.springframework.apifirst.apifirstserver.jpa.controller;

import ch.guru.springframework.apifirst.apifirstserver.jpa.bootstrap.DataLoader;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.servlet.LogbookFilter;

import static ch.guru.springframework.apifirst.apifirstserver.jpa.config.OpenApiValidationConfig.OPENAPI_SPECIFICATION_URL;
import static com.atlassian.oai.validator.mockmvc.OpenApiValidationMatchers.openApi;
import static org.hamcrest.Matchers.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(DataLoader.class)
class CategoryControllerTest {

    @Autowired
    WebApplicationContext wac;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    Filter validationFilter;

    public MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        objectMapper.configure(JsonParser.Feature.INCLUDE_SOURCE_IN_LOCATION, true);
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
            .addFilter(validationFilter)
            .addFilter(new LogbookFilter(Logbook.create()))    
            .build();
    }

    @Test
    void testListCategories() throws Exception {
        mockMvc.perform(get(CategoryController.CATEGORY_BASE_URL)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()", greaterThan(2)))
            .andExpect(openApi().isValid(OPENAPI_SPECIFICATION_URL));
    }
}
