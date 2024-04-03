package com.mjc.school.service.errorsexceptions;

public class ValidatorException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public ValidatorException(String msg) {
        super(msg);
    }
}