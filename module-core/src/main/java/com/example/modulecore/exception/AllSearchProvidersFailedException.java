package com.example.modulecore.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;



@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class AllSearchProvidersFailedException extends RuntimeException {
    public AllSearchProvidersFailedException(String message) {
        super(message);
    }
}