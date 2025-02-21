package ch.guru.springframework.apifirst.apifirstserver.jpa.config;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.springmvc.OpenApiValidationFilter;
import com.atlassian.oai.validator.springmvc.OpenApiValidationInterceptor;
import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

@Configuration
public class OpenApiValidationConfig {
    
    public static final String OPENAPI_SPECIFICATION_URL;

    static {
        try {
            OPENAPI_SPECIFICATION_URL = new ClassPathResource("openapi.yaml").getFile().toURI().toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public Filter validationFilter() {
        return new OpenApiValidationFilter(
            true, // enable request validation
            true  // enable response validation
        );
    }

    @Bean
    // we have currently a problem with that validator which does not validate the responses against the OpenAPI specification
    // therefore we introduced the workaround with the swagger-request-validator-mockmvc. See the controller tests where we
    // added the validation explicitly at the end of each test: andExpect(openApi().isValid(OPENAPI_SPECIFICATION_URL))
    public WebMvcConfigurer openAPIValidationInterceptor() throws IOException {
        OpenApiInteractionValidator validator = OpenApiInteractionValidator
            .createForSpecificationUrl(OPENAPI_SPECIFICATION_URL)
            .build();
        OpenApiValidationInterceptor interceptor = new OpenApiValidationInterceptor(validator);

        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(interceptor);
            }
        };
    }
    
}
