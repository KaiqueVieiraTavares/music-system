package com.ms.musicservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandleRestControllerAdvice {

    @ExceptionHandler(MusicNotFoundException.class)
    public ProblemDetail handleMusicNotFound(MusicNotFoundException e){
        var exception = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        exception.setTitle("Music not found");
        return exception;
    }
    @ExceptionHandler(MusicAlreadyExistsException.class)
    public ProblemDetail handleMusicAlreadyExists(MusicAlreadyExistsException e){
        var exception = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
        exception.setTitle("Music alerady exists");
        return exception;
    }
}
