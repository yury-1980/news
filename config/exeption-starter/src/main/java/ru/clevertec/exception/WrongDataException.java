package ru.clevertec.exception;

public class WrongDataException extends RuntimeException {

    public WrongDataException(String message, Class<?> clazz) {
        super(message + " " + clazz.getSimpleName());
    }

    public static WrongDataException of(Class<?> clazz) {
        return new WrongDataException("Wrong data", clazz);
    }
}
