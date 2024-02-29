package ru.clevertec.exeption;

public class EntityNotFoundExeption extends RuntimeException {

    public EntityNotFoundExeption(String message, Class<?> clazz) {
        super(message + " " + clazz.getSimpleName());
    }

    public static EntityNotFoundExeption of(Class<?> clazz) {
        return new EntityNotFoundExeption("Object not found", clazz);
    }
}
