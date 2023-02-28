package com.example.budgetmanager.config;

import com.example.budgetmanager.exception.BudgetAlreadyExistException;
import com.example.budgetmanager.exception.BudgetNotFoundException;
import com.example.budgetmanager.exception.BudgetExceededException;
import com.example.budgetmanager.exception.CustomerAlreadyExistException;
import com.example.budgetmanager.model.ApiError;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatus status,
                                                                  @NonNull WebRequest request) {
        Map<String, String> errorDetails = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errorDetails.put(fieldName, errorMessage);
        });
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, errorDetails);
        logger.error(ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {CustomerAlreadyExistException.class, BudgetAlreadyExistException.class, BudgetExceededException.class})
    protected ResponseEntity<ApiError> handleInvalidDataExceptions(RuntimeException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage());
        logger.error(ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {BudgetNotFoundException.class})
    protected ResponseEntity<ApiError> handleNotFoundExceptions(RuntimeException ex) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage());
        logger.error(ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<ApiError> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        ApiError apiError = new ApiError(HttpStatus.CONFLICT, "Database error, a data conflict has occurred");
        logger.error(ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TransactionSystemException.class)
    protected ResponseEntity<ApiError> handleTransactionException(TransactionSystemException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Error transaction system, try again later");
        logger.error(ex.getMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ApiError> handleDefaultConstraintsViolation(ConstraintViolationException ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Unknown error occurred");
        logger.error(ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiError> handleDefaultException(Exception ex) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Unknown error occurred");
        logger.error(ex.getMessage());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}
