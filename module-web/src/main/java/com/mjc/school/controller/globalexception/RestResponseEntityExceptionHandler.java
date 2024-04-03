package com.mjc.school.controller.globalexception;

import com.mjc.school.service.errorsexceptions.ApiUriException;
import com.mjc.school.service.errorsexceptions.Errors;
import com.mjc.school.service.errorsexceptions.NotFoundException;
import com.mjc.school.service.errorsexceptions.ValidatorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException nfe) {
        return buildErrorResponse(Errors.ERROR_RESOURCE_NOT_FOUND.getErrorCode(),
                Errors.ERROR_RESOURCE_NOT_FOUND.getErrorMessage(), nfe.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidatorException.class)
    public ResponseEntity<ErrorResponse> handleValidatorException(ValidatorException ve) {
        return buildErrorResponse(Errors.VALIDATION_ERROR.getErrorCode(),
                Errors.VALIDATION_ERROR.getErrorMessage(), ve.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApiUriException.class)
    public ResponseEntity<ErrorResponse> handleApiUriException(ApiUriException e) {
        return buildErrorResponse(Errors.ERROR_WRONG_API_URI.getErrorCode(),
                Errors.ERROR_WRONG_API_URI.getErrorMessage(), e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(Exception e) {
        return buildErrorResponse(Errors.UNEXPECTED_ERROR.getErrorCode(),
                Errors.UNEXPECTED_ERROR.getErrorMessage(), e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(String code, String errorMessage,
                                                             String errorDetails, HttpStatus status) {
        return new ResponseEntity<>(new ErrorResponse(code, errorMessage, errorDetails), status);
    }
}