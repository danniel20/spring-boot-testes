package com.securityteste.securityspringteste.exception;

import java.util.HashMap;
import java.util.Map;

import com.securityteste.securityspringteste.response.ResponseHandler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import org.springframework.validation.FieldError;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<Object> handleAccessDeniedException( Exception ex, WebRequest request) {
        return ResponseHandler.generateResponse("Acesso Negado", HttpStatus.FORBIDDEN, new HttpHeaders());
    }
    
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid( 
        MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

            Map<String, String> errors = new HashMap<>();
            ex.getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
            
            return ResponseHandler.generateResponse(null, HttpStatus.BAD_REQUEST, errors);
        }
}
