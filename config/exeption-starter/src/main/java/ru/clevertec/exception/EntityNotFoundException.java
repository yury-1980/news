package ru.clevertec.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message, Class<?> clazz) {
        super(message + " " + clazz.getSimpleName());
    }

    public static EntityNotFoundException of(Class<?> clazz) {
        return new EntityNotFoundException("Object not found", clazz);
    }
}
