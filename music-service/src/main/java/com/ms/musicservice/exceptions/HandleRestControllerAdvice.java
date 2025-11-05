package com.ms.musicservice.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.UUID;

@RestControllerAdvice
public class HandleRestControllerAdvice {
    private static final Logger logger = LoggerFactory.getLogger(HandleRestControllerAdvice.class);

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception e){
        String traceId = UUID.randomUUID().toString();
        logger.error("Unexpected error occurred. traceId={}, exceptionType={}",
                traceId, e.getClass().getSimpleName(), e);
        return buildProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred, please try again later",
                "Internal Server Error",
                traceId
        );
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException e){
        String traceId = UUID.randomUUID().toString();
        logger.error("Data violation. traceId={}, details={}", traceId, e.getMessage());
        return buildProblemDetail(HttpStatus.CONFLICT, "Has occurred an data violation", "Data violation", traceId);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValid(MethodArgumentNotValidException e){
        String traceId = UUID.randomUUID().toString();
        var details = e.getBindingResult().getFieldErrors().stream().map(fieldError -> "Field: " + fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();
        logger.warn("Method argument not valid. traceId={}, details={}", traceId, details);

        var problem = buildProblemDetail(HttpStatus.BAD_REQUEST, "The method argument is not valid", "Method argument is not valid", traceId);
        problem.setProperty("errors", details);
        return problem;
    }
    @ExceptionHandler(MusicNotFoundException.class)
    public ProblemDetail handleMusicNotFound(MusicNotFoundException e){
        String traceId = UUID.randomUUID().toString();
        logger.warn("Music not found. traceId={}, details={}", traceId, e.getMessage());
        return buildProblemDetail(
                HttpStatus.NOT_FOUND,
                "The music was not found",
                "Music not found",
                traceId
        );
    }

    @ExceptionHandler(MusicAlreadyExistsException.class)
    public ProblemDetail handleMusicAlreadyExists(MusicAlreadyExistsException e){
        String traceId = UUID.randomUUID().toString();
        logger.warn("Music already exists. traceId={}, details={}", traceId, e.getMessage());
        return buildProblemDetail(
                HttpStatus.CONFLICT,
                "This song has already been registered for this artist",
                "Music already exists",
                traceId
        );
    }

    private ProblemDetail buildProblemDetail(HttpStatus httpStatus, String message, String title, String traceId){
        var problem = ProblemDetail.forStatusAndDetail(httpStatus, message);
        problem.setTitle(title);
        problem.setProperty("timestamp", LocalDateTime.now());
        problem.setProperty("traceId", traceId);
        return problem;
    }
}
