package ru.kravchenko.common.exception;

import lombok.Getter;

@Getter
public class DefaultHttpException extends RuntimeException {
    private final int code;

    public DefaultHttpException(String message, Throwable cause) {
        super(message, cause);
        this.code = 400;
    }

    public DefaultHttpException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }
}
