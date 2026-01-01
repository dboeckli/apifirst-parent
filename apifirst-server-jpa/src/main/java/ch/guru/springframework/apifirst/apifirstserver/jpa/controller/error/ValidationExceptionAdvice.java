package ch.guru.springframework.apifirst.apifirstserver.jpa.controller.error;

import ch.guru.springframework.apifirst.model.ProblemDto;
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
    public ResponseEntity<ProblemDto> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        String detail = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(this::formatFieldError)
            .collect(Collectors.joining("; "));

        ProblemDto body = new ProblemDto()
            .type(URI.create("about:blank"))
            .title(status.getReasonPhrase())
            .status(status.value())
            .detail(detail.isBlank() ? "Validation failed" : detail)
            .instance(request.getRequestURI());

        return ResponseEntity.status(status)
            .contentType(PROBLEM_JSON)
            .body(body);
    }

    private String formatFieldError(FieldError fe) {
        String defaultMessage = fe.getDefaultMessage();
        return fe.getField() + ": " + (defaultMessage == null ? "invalid" : defaultMessage);
    }
}