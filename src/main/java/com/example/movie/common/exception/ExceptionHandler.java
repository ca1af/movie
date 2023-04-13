package com.example.movie.common.exception;

import com.example.movie.common.dto.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @org.springframework.web.bind.annotation.ExceptionHandler(NoSuchElementException.class)
    public ExceptionResponse handleNoSuchElementException(NoSuchElementException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return new ExceptionResponse(status.value(), status.name(), 404, e.getMessage());
    }
}
