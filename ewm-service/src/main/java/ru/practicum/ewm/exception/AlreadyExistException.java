package ru.practicum.ewm.exception;

public class AlreadyExistException extends RuntimeException {
    public AlreadyExistException(Class<?> entityClass, String message) {
        super(entityClass.getSimpleName()  + message + " already exist. ");
    }
}
