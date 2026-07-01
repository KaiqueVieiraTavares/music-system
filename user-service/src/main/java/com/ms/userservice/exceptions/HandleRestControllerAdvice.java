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
        return buildProblem(HttpStatus.CONFLICT, "Email already exists", e.getMessage());
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleUserNotFound(UserNotFoundException e){
        return buildProblem(HttpStatus.NOT_FOUND, "User not found", e.getMessage());
    }
    @ExceptionHandler(EmailNotFoundException.class)
    public ProblemDetail handleEmailNotFound(EmailNotFoundException e){
        return buildProblem(HttpStatus.NOT_FOUND,"Email not found", e.getMessage());
    }
    private ProblemDetail buildProblem(HttpStatus status, String title, String detail){
        var problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setTitle(title);
        problem.setProperty("timestamp", Instant.now());
        return problem;
    }
}
