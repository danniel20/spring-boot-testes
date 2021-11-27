package com.securityteste.securityspringteste.exceptions;

import java.util.HashMap;
import java.util.Map;

import com.securityteste.securityspringteste.response.ResponseHandler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<Object> handleAccessDeniedException( Exception ex, WebRequest request) {
        return ResponseHandler.generateResponse("Você não tem permissão de acesso!", HttpStatus.FORBIDDEN, new HttpHeaders());
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class})
    public ResponseEntity<Object> handleMethodArgumentNotValid( Exception ex, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ((BindException) ex).getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        return ResponseHandler.generateResponse("Erro de Validação", HttpStatus.BAD_REQUEST, errors);
    }

    @ExceptionHandler({ NullPointerException.class})
    public ResponseEntity<Object> handleNullPointerException(Exception ex, WebRequest request){
        return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    @ExceptionHandler({ java.lang.NumberFormatException.class})
    public ResponseEntity<Object> handleNumberFormatException(Exception ex, WebRequest request){
        return ResponseHandler.generateResponse("Não foi possível converter a String para valor numérico!", HttpStatus.BAD_REQUEST, null);
    }

    @ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
    public ResponseEntity<Object> handleHttpRequestMethodNotSupportedException(Exception ex, WebRequest request){
        return ResponseHandler.generateResponse(ex.getMessage(), HttpStatus.BAD_REQUEST, null);
    }

    @ExceptionHandler({ HttpMessageNotReadableException.class })
    public ResponseEntity<Object> handleHttpRequestMethodNotReadableException(Exception ex, WebRequest request){
        return ResponseHandler.generateResponse("Erro de formatação no arquivo JSON!", HttpStatus.BAD_REQUEST, null);
    }

}
