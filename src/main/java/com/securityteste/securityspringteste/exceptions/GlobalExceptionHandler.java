package com.securityteste.securityspringteste.exceptions;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

import com.securityteste.securityspringteste.response.ResponseHandler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

//import io.jsonwebtoken.ExpiredJwtException;

import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<Object> handleAccessDeniedException( Exception ex, WebRequest request) {
        return ResponseHandler.generateResponse("Acesso Negado", HttpStatus.FORBIDDEN, new HttpHeaders());
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public ResponseEntity<Object> handleMethodArgumentNotValid( Exception ex, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ((BindException) ex).getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        return ResponseHandler.generateResponse("Erro de Validação", HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler({ NullPointerException.class })
    public ResponseEntity<Object> handleNullPointerException(Exception ex, WebRequest request){
        return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.NOT_FOUND, null);
    }

    @ExceptionHandler({ EntityNotFoundException.class })
    public ResponseEntity<Object> handleEntityNotFoundException(Exception ex, WebRequest request){
        return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.NOT_FOUND, null);
    }

    @ExceptionHandler({ IllegalArgumentException.class })
    public ResponseEntity<Object> handleIllegalArgumentException(Exception ex, WebRequest request){
        return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    @ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
    public ResponseEntity<Object> handleHttpRequestMethodNotSupportedException(Exception ex, WebRequest request){
        return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    // @ExceptionHandler({ ExpiredJwtException.class })
    // public ResponseEntity<Object> handleEpiredJwtException(Exception ex, WebRequest request){
    //     return ResponseHandler.generateResponse("Token Expirado!", HttpStatus.INTERNAL_SERVER_ERROR, null);
    // }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleException(Exception ex, WebRequest request){
        return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

}
