package ch.guru.springframework.apifirst.apifirstserver.config;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.springmvc.OpenApiValidationFilter;
import com.atlassian.oai.validator.springmvc.OpenApiValidationInterceptor;
import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class OpenApiValidationConfig {
    
    @Bean
    public Filter validationFilter() {
        return new OpenApiValidationFilter(
            false, // disable request validation: TODO: enable request validation. is currently failing
            true  // enable response validation
        );
    }

    @Bean
    public WebMvcConfigurer openAPIValidationInterceptor() {
        OpenApiInteractionValidator validator = OpenApiInteractionValidator.createForSpecificationUrl("https://dboeckli.redocly.app/_spec/openapi/openapi.yaml")
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
