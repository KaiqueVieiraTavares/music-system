package com.ms.userservice.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class HandleRestControllerAdvice {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ProblemDetail handleEmailAlreadyExists(EmailAlreadyExistsException e){
        var exception = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
        exception.setTitle("Email already exists");
        exception.setProperty("timestamp", Instant.now());
        return exception;
    }
}
