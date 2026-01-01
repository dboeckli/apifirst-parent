package ch.guru.springframework.apifirst.apifirstserver.jpa.controller.error;

import ch.guru.springframework.apifirst.apifirstserver.jpa.service.error.ConflictException;
import ch.guru.springframework.apifirst.apifirstserver.jpa.service.error.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
public class GlobalExceptionAdvice {

    private static final MediaType PROBLEM_JSON = MediaType.parseMediaType("application/problem+json");

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Problem> handleNotFound(NotFoundException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        Problem body = Problem.of(
            URI.create("about:blank"),
            status.getReasonPhrase(),
            status.value(),
            safeMessage(ex, "Not Found"),
            request.getRequestURI()
        );

        return ResponseEntity.status(status)
            .contentType(PROBLEM_JSON)
            .body(body);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Problem> handleConflict(ConflictException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;

        Problem body = Problem.of(
            URI.create("about:blank"),
            status.getReasonPhrase(),
            status.value(),
            safeMessage(ex, "Conflict"),
            request.getRequestURI()
        );

        return ResponseEntity.status(status)
            .contentType(PROBLEM_JSON)
            .body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Problem> handleBadRequest(IllegalArgumentException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        Problem body = Problem.of(
            URI.create("about:blank"),
            status.getReasonPhrase(),
            status.value(),
            safeMessage(ex, "Bad Request"),
            request.getRequestURI()
        );

        return ResponseEntity.status(status)
            .contentType(PROBLEM_JSON)
            .body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Problem> handleUnexpected(Exception ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        Problem body = Problem.of(
            URI.create("about:blank"),
            status.getReasonPhrase(),
            status.value(),
            "Unexpected error",
            request.getRequestURI()
        );

        return ResponseEntity.status(status)
            .contentType(PROBLEM_JSON)
            .body(body);
    }

    private String safeMessage(Exception ex, String fallback) {
        String msg = ex.getMessage();
        return (msg == null || msg.isBlank()) ? fallback : msg;
    }
}