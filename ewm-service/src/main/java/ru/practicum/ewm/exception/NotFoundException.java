package ru.practicum.ewm.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(Class<?> entityClass, String message) {
        super(entityClass.getSimpleName()  + message + "was not found. ");
    }
}
