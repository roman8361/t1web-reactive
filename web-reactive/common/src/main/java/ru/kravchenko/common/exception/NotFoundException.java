package ru.kravchenko.common.exception;

public class NotFoundException extends RuntimeException {
    private final String code;

    public NotFoundException(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
