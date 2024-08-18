package com.search.vehicle.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;

/**
 * Exception handler controller, manually handles exceptions.
 * @author Vansh Pratap Singh
 */
@RestControllerAdvice
public class ExceptionHandlerController {

    /**
     * Handles the MethodArgumentNotValidException exception (when any argument fails some validation).
     * @param ex                MethodArgumentNotValidException.
     * @return                  Response object.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        Map<String, Object> errors = new LinkedHashMap<>();

        List<Map<String, String>> errorList = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {

            Map<String, String> currErrorObj = new HashMap<>();
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            currErrorObj.put(fieldName, errorMessage);
            errorList.add(currErrorObj);

        });

        errors.put("success", false);
        errors.put("message", "Validation check(s) failed.");
        errors.put("errors", errorList);

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);

    }

    /**
     * Handles the ConstraintViolationException exception (when any argument fails some validation).
     * @param ex                ConstraintViolationException.
     * @return                  Response object.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, Object>> handleConstraintViolationException(ConstraintViolationException ex) {

        Map<String, Object> errors = new LinkedHashMap<>();

        List<Map<String, String>> errorList = new ArrayList<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {

            Map<String, String> currErrorObj = new HashMap<>();
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            currErrorObj.put(fieldName, errorMessage);
            errorList.add(currErrorObj);

        }

        errors.put("success", false);
        errors.put("message", "Validation check(s) failed.");
        errors.put("errors", errorList);

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);

    }

}
