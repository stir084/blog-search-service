package com.example.modulecommon.exception;


import org.springframework.http.HttpStatusCode;

public class BlogSearchEngineResponseException extends RuntimeException {
    private final HttpStatusCode httpStatusCode;
    public BlogSearchEngineResponseException(String message, HttpStatusCode status) {
        super(message);
        this.httpStatusCode = status;
    }

    public HttpStatusCode getHttpStatusCode() {
        return httpStatusCode;
    }
}