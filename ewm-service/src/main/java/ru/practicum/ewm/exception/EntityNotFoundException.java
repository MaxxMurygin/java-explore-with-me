package ru.practicum.ewm.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Class<?> entityClass, String message) {
        super("Entity " + entityClass.getSimpleName() + " not found. " + message);
    }
}
