package ch.guru.springframework.apifirst.apifirstserver.jpa.controller.error;

import ch.guru.springframework.apifirst.apifirstserver.jpa.service.error.ConflictException;
import ch.guru.springframework.apifirst.apifirstserver.jpa.service.error.NotFoundException;
import ch.guru.springframework.apifirst.model.ProblemDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionAdviceTest {

    private final GlobalExceptionAdvice advice = new GlobalExceptionAdvice();

    @Test
    void handleNotFoundReturnsMessage() {
        ProblemDto body = handle(advice.handleNotFound(new NotFoundException("Customer not found"), request()));

        assertThat(body.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(body.getTitle()).isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());
        assertThat(body.getDetail()).isEqualTo("Customer not found");
    }

    @Test
    void handleNotFoundUsesFallbackForBlankMessage() {
        ProblemDto body = handle(advice.handleNotFound(new NotFoundException(), request()));

        assertThat(body.getDetail()).isEqualTo("Not Found");
    }

    @Test
    void handleConflictReturnsMessage() {
        ProblemDto body = handle(advice.handleConflict(new ConflictException("Already exists"), request()));

        assertThat(body.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(body.getDetail()).isEqualTo("Already exists");
    }

    @Test
    void handleBadRequestReturnsMessage() {
        ProblemDto body = handle(advice.handleBadRequest(new IllegalArgumentException("Invalid value"), request()));

        assertThat(body.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(body.getDetail()).isEqualTo("Invalid value");
    }

    @Test
    void handleUnexpectedUsesFallbackForBlankMessage() {
        ProblemDto body = handle(advice.handleUnexpected(new RuntimeException(" "), request()));

        assertThat(body.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(body.getDetail()).isEqualTo("Unexpected error");
    }

    @Test
    void handleUnexpectedReturnsMessage() {
        ProblemDto body = handle(advice.handleUnexpected(new RuntimeException("boom"), request()));

        assertThat(body.getDetail()).isEqualTo("boom");
    }

    private ProblemDto handle(ResponseEntity<ProblemDto> response) {
        assertThat(response.getHeaders().getContentType())
            .isEqualTo(MediaType.parseMediaType("application/problem+json"));
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getInstance()).isEqualTo("/api/v1/customers");
        return response.getBody();
    }

    private MockHttpServletRequest request() {
        return new MockHttpServletRequest("GET", "/api/v1/customers");
    }

}
