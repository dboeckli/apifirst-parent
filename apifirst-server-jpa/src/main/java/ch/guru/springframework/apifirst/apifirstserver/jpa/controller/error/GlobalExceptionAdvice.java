package ch.guru.springframework.apifirst.apifirstserver.jpa.controller.error;

import ch.guru.springframework.apifirst.apifirstserver.jpa.service.error.ConflictException;
import ch.guru.springframework.apifirst.apifirstserver.jpa.service.error.NotFoundException;
import ch.guru.springframework.apifirst.model.ProblemDto;
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
    public ResponseEntity<ProblemDto> handleNotFound(NotFoundException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        ProblemDto body = new ProblemDto()
            .type(URI.create("about:blank"))
            .title(status.getReasonPhrase())
            .status(status.value())
            .detail(safeMessage(ex, "Not Found"))
            .instance(request.getRequestURI());

        return ResponseEntity.status(status)
            .contentType(PROBLEM_JSON)
            .body(body);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ProblemDto> handleConflict(ConflictException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;

        ProblemDto body = new ProblemDto()
            .type(URI.create("about:blank"))
            .title(status.getReasonPhrase())
            .status(status.value())
            .detail(safeMessage(ex, "Conflict"))
            .instance(request.getRequestURI());

        return ResponseEntity.status(status)
            .contentType(PROBLEM_JSON)
            .body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDto> handleBadRequest(IllegalArgumentException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ProblemDto body = new ProblemDto()
            .type(URI.create("about:blank"))
            .title(status.getReasonPhrase())
            .status(status.value())
            .detail(safeMessage(ex, "Bad Request"))
            .instance(request.getRequestURI());

        return ResponseEntity.status(status)
            .contentType(PROBLEM_JSON)
            .body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDto> handleUnexpected(Exception ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ProblemDto body = new ProblemDto()
            .type(URI.create("about:blank"))
            .title(status.getReasonPhrase())
            .status(status.value())
            .detail(safeMessage(ex, "Unexpected error"))
            .instance(request.getRequestURI());

        return ResponseEntity.status(status)
            .contentType(PROBLEM_JSON)
            .body(body);
    }

    private String safeMessage(Exception ex, String fallback) {
        String msg = ex.getMessage();
        return (msg == null || msg.isBlank()) ? fallback : msg;
    }
}