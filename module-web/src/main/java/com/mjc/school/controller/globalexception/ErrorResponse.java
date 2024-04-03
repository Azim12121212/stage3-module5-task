package com.mjc.school.controller.globalexception;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final String code;
    private final String message;
    private String errorDetails;

    public ErrorResponse(String code, String message, String errorDetails) {
        this.code = code;
        this.message = message;
        this.errorDetails = errorDetails;
    }
}