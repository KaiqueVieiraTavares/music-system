package com.ms.artistservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class HandleRestControllerAdvice {


    @ExceptionHandler(ArtistNotFoundException.class)
    public ProblemDetail handleArtistNotFound(ArtistNotFoundException e){
        var exception = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        exception.setTitle("Artist not found");
        return exception;
    }
}
