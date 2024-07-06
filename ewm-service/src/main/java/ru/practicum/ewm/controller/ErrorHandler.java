package ru.practicum.ewm.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.exception.AlreadyExistException;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.model.ApiError;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    private final DateTimeFormatter formatter;

    public ErrorHandler(@Value("${ewm.date.format}")String format) {
        this.formatter =  DateTimeFormatter.ofPattern(format);
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotExist(final RuntimeException e) {
        log.debug(e.getMessage());
        return ApiError.builder()
                .errors(Arrays.toString(e.getStackTrace()))
                .message(e.getMessage())
                .reason("The required object was not found.")
                .status(HttpStatus.NOT_FOUND)
                .timestamp(LocalDateTime.now().format(formatter))
                .build();
    }

    @ExceptionHandler({AlreadyExistException.class, DataIntegrityViolationException.class,
            ValidationException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleAlreadyExist(final RuntimeException e) {
        log.info(e.getMessage());
        return ApiError.builder()
                .errors(Arrays.toString(e.getStackTrace()))
                .message(e.getMessage())
                .reason("Integrity constraint has been violated.")
                .status(HttpStatus.CONFLICT)
                .timestamp(LocalDateTime.now().format(formatter))
                .build();
    }

//    @ExceptionHandler({ForbiddenException.class})
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    public ErrorResponse handleForbidden(final RuntimeException e) {
//        log.debug(e.getMessage());
//        return new ErrorResponse(e.getMessage());
//    }

    @ExceptionHandler({MethodArgumentNotValidException.class, NumberFormatException.class,
            BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleOtherException(final Exception e) {
        log.debug(e.getMessage());
        return ApiError.builder()
                .errors(Arrays.toString(e.getStackTrace()))
                .message(e.getMessage())
                .reason("Incorrectly made request.")
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now().format(formatter))
                .build();
    }
}
