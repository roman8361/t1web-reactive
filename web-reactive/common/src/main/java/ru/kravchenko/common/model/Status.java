package ru.kravchenko.common.model;

public record Status(boolean status, String code, String description) {
    public static Status statusError(String code, String description) {
        return new Status(false, code, description);
    }

    public static Status statusSuccess(String code, String description) {
        return new Status(true, code, description);
    }
}
