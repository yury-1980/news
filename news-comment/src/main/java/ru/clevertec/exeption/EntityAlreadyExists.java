package ru.clevertec.exeption;

public class EntityAlreadyExists extends RuntimeException {

    public EntityAlreadyExists(String message, Class<?> clazz) {
        super(message + " " + clazz.getSimpleName());
    }

    public static EntityNotFoundExeption of(Class<?> clazz) {
        return new EntityNotFoundExeption("Object Already Exists:", clazz);
    }
}