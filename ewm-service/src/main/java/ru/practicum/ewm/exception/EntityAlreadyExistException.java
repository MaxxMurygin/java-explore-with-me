package ru.practicum.ewm.exception;

public class EntityAlreadyExistException extends RuntimeException {
    public EntityAlreadyExistException(Class<?> entityClass, String message) {
        super(entityClass.getSimpleName()  + message + " already exist. ");
    }
}
