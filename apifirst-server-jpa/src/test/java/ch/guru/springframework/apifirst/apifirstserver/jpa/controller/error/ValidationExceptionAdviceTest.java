package ch.guru.springframework.apifirst.apifirstserver.jpa.controller.error;

import ch.guru.springframework.apifirst.model.ProblemDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.assertj.core.api.Assertions.assertThat;

class ValidationExceptionAdviceTest {

    private final ValidationExceptionAdvice advice = new ValidationExceptionAdvice();

    @Test
    void handleValidationReturnsBadRequestWithFieldErrors() {
        ProblemDto body = handle(validationException(new FieldError("target", "name", "must not be null")));

        assertThat(body.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(body.getDetail()).isEqualTo("name: must not be null");
    }

    @Test
    void handleValidationUsesInvalidForMissingMessage() {
        ProblemDto body = handle(validationException(new FieldError("target", "name", null)));

        assertThat(body.getDetail()).isEqualTo("name: invalid");
    }

    @Test
    void handleValidationUsesFallbackWithoutFieldErrors() {
        ProblemDto body = handle(validationException());

        assertThat(body.getDetail()).isEqualTo("Validation failed");
    }

    private ProblemDto handle(MethodArgumentNotValidException exception) {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/v1/customers");
        ResponseEntity<ProblemDto> response = advice.handleValidation(exception, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getHeaders().getContentType())
            .isEqualTo(MediaType.parseMediaType("application/problem+json"));
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getInstance()).isEqualTo("/api/v1/customers");
        return response.getBody();
    }

    private MethodArgumentNotValidException validationException(FieldError... fieldErrors) {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "target");
        for (FieldError fieldError : fieldErrors) {
            bindingResult.addError(fieldError);
        }
        return new MethodArgumentNotValidException(null, bindingResult);
    }

}
