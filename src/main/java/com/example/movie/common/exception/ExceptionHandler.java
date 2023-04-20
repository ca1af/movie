package com.example.movie.common.exception;

import com.example.movie.common.dto.ExceptionResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @org.springframework.web.bind.annotation.ExceptionHandler(NoSuchElementException.class)
    public ExceptionResponse noSuchElementExceptionHandler(NoSuchElementException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return new ExceptionResponse(status.value(), status.name(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @org.springframework.web.bind.annotation.ExceptionHandler(DataIntegrityViolationException.class)
    public ExceptionResponse dataIntegrityViolationExceptionHandler(DataIntegrityViolationException e) {
        HttpStatus status = HttpStatus.CONFLICT;

        return new ExceptionResponse(status.value(), status.name(), e.getMessage());
    }
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionResponse methodArgumentNotValidException(MethodArgumentNotValidException e){
        HttpStatus status = HttpStatus.BAD_REQUEST;

        return new ExceptionResponse(status.value(), status.name(), e.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    public ExceptionResponse illegalArgumentException(IllegalArgumentException e){
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ExceptionResponse(status.value(), status.name(), e.getMessage());
    }
}
