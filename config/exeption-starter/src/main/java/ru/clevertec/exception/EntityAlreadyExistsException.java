package ru.clevertec.exception;

public class EntityAlreadyExistsException extends RuntimeException {

    public EntityAlreadyExistsException(String message, Class<?> clazz) {
        super(message + " " + clazz.getSimpleName());
    }

    public static EntityNotFoundException of(Class<?> clazz) {
        return new EntityNotFoundException("Object Already Exists:", clazz);
    }
}