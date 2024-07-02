package ru.practicum.ewm.exception;

public class EntityAlreadyExistException extends RuntimeException {
    public EntityAlreadyExistException(Class<?> entityClass, String message) {
        super("Entity " + entityClass.getSimpleName() + " already exist. " + message);
    }
}
