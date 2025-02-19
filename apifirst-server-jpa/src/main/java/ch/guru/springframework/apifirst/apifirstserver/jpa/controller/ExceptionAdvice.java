package ch.guru.springframework.apifirst.apifirstserver.jpa.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class ExceptionAdvice {

    @ResponseStatus(value= HttpStatus.CONFLICT, reason="Data integrity violation")  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public void conflictHandler() {
        log.error("Data integrity violation occurred");
    }
    
}
