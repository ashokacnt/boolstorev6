package com.example.common;

public class BadRequestException extends BusinessException {
    public BadRequestException(String m) {
        super(m);
    }
}
