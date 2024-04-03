package com.mjc.school.service.errorsexceptions;

public class ApiUriException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public ApiUriException(String msg) {
        super(msg);
    }
}