package com.wallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.dao.DataIntegrityViolationException;
import com.wallet.dto.ApiResponse;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ApiResponse<Object> handleCustomException(CustomException ex) {

        // Map<String, Object> response = new HashMap<>();
        // response.put("message", ex.getMessage());
        // response.put("status", HttpStatus.BAD_REQUEST.value());
        return new ApiResponse<>(false, ex.getMessage(), null);

        // return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Object> handleCustomException(MethodArgumentNotValidException ex) {

        // Map<String, Object> response = new HashMap<>();
        // String errorMessage = ex.getBindingResult()
        //                     .getFieldError()
        //                     .getDefaultMessage();

        // response.put("message", errorMessage);
        // response.put("status", HttpStatus.BAD_REQUEST.value());

        // return response;

        String errorMessage = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .findFirst()
            .map(error -> error.getDefaultMessage())
            .orElse("Validation error");

        return new ApiResponse<>(false, errorMessage, null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ApiResponse<Object> handleDuplicateEmail(DataIntegrityViolationException ex) {

        // Map<String, Object> response = new HashMap<>();
        // response.put("message", "Email already exists");
        // response.put("status", 400);

        // return response;

        return new ApiResponse<>(false, "Email already exists", null);
    }
    // @ExceptionHandler(Exception.class)
    // public Map<String, Object> handleGenericException(Exception ex) {

    //     Map<String, Object> response = new HashMap<>();
    //     response.put("message", "Something went wrong");
    //     response.put("status", 500);

    //     return response;
        // return new ApiResponse<>(false, "Something went wrong", null);
    // }
}