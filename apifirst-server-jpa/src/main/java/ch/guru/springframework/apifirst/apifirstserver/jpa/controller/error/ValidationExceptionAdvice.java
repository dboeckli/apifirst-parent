package ch.guru.springframework.apifirst.apifirstserver.jpa.controller.error;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ValidationExceptionAdvice {

    private static final MediaType PROBLEM_JSON = MediaType.parseMediaType("application/problem+json");

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Problem> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        String detail = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(this::formatFieldError)
            .collect(Collectors.joining("; "));

        Problem body = Problem.of(
            URI.create("about:blank"),
            status.getReasonPhrase(),
            status.value(),
            detail.isBlank() ? "Validation failed" : detail,
            request.getRequestURI()
        );

        return ResponseEntity.status(status)
            .contentType(PROBLEM_JSON)
            .body(body);
    }

    private String formatFieldError(FieldError fe) {
        String defaultMessage = fe.getDefaultMessage();
        return fe.getField() + ": " + (defaultMessage == null ? "invalid" : defaultMessage);
    }
}