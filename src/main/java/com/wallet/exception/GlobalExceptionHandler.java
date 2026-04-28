package com.wallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public Map<String, Object> handleCustomException(CustomException ex) {

        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        response.put("status", HttpStatus.BAD_REQUEST.value());

        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Object> handleCustomException(MethodArgumentNotValidException ex) {

        Map<String, Object> response = new HashMap<>();
        String errorMessage = ex.getBindingResult()
                            .getFieldError()
                            .getDefaultMessage();

        response.put("message", errorMessage);
        response.put("status", HttpStatus.BAD_REQUEST.value());

        return response;
    }
    // @ExceptionHandler(Exception.class)
    // public Map<String, Object> handleGenericException(Exception ex) {

    //     Map<String, Object> response = new HashMap<>();
    //     response.put("message", "Something went wrong");
    //     response.put("status", 500);

    //     return response;
    // }
}