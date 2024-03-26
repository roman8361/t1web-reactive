package ru.kravchenko.common.exception;

public class LimitException extends RuntimeException {
    private final String code;

    public LimitException(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
