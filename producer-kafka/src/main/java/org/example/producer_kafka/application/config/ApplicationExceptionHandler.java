package org.example.producer_kafka.application.config;

import org.example.items_api_service.IncorrectCreateItemRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler
{
    
    @ExceptionHandler(IncorrectCreateItemRequestException.class)
    public ResponseEntity<String> handleApplicationException(Exception exception)
    {
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
