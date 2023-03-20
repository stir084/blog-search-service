package com.example.modulecommon.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(AllSearchProvidersFailedException.class)
    public ResponseEntity<ErrorResponse> handleMemberAlreadyExistsException(AllSearchProvidersFailedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    @ExceptionHandler(BlogSearchEngineResponseException.class)
    public ResponseEntity<ErrorResponse> handleWebClientResponseException(BlogSearchEngineResponseException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getHttpStatusCode().value(), ex.getMessage());
        return ResponseEntity.status(ex.getHttpStatusCode()).body(errorResponse);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ErrorResponse> handleNumberFormatException() {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Invalid parameter value");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValid() {
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Validation error");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadable() {
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Malformed request");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupported() {
        ErrorResponse response = new ErrorResponse(HttpStatus.METHOD_NOT_ALLOWED.value(), "Unsupported method");
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    @ExceptionHandler({HttpMediaTypeNotSupportedException.class, InvalidMediaTypeException.class})
    protected ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupported() {
        ErrorResponse response = new ErrorResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "Unsupported media type");
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(response);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class, ServletRequestBindingException.class})
    protected ResponseEntity<ErrorResponse> handleMissingServletRequestParameter() {
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Missing parameter");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}