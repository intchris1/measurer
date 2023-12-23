package com.offsidegaming.measurer.web.exception;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class ServiceExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex) {
        List<Pair<String, String>> fieldErrors = ex.getBindingResult().getFieldErrors()
                .stream().map(it -> Pair.of(it.getField(), it.getDefaultMessage()))
                .sorted(java.util.Map.Entry.comparingByKey()).toList();
        List<String> globalErrors = ex.getBindingResult().getGlobalErrors()
                .stream().map(DefaultMessageSourceResolvable::getDefaultMessage)
                .filter(Objects::nonNull)
                .sorted()
                .toList();
        ProblemDetail response = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase());
        if (!fieldErrors.isEmpty()) {
            response.setProperty("fieldErrors", fieldErrors);
        }
        if (!globalErrors.isEmpty()) {
            response.setProperty("globalErrors", globalErrors);
        }
        return response;
    }
}