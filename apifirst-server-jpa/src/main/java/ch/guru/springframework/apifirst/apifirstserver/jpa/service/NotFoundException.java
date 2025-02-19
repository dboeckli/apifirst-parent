package ch.guru.springframework.apifirst.apifirstserver.jpa.service;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = org.springframework.http.HttpStatus.NOT_FOUND, reason = "Value Not Found")
public class NotFoundException extends RuntimeException {

    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause){
        super(message, cause);
    }

    public NotFoundException(Throwable cause){
        super(cause);
    }

    public NotFoundException(String message, Throwable cause,
                             boolean enableSuppression,
                             boolean writableStackTrace){
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
