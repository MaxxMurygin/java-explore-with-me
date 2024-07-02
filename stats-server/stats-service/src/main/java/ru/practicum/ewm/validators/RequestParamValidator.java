package ru.practicum.ewm.validators;

import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.model.HitMapper;

import java.time.LocalDateTime;
import java.util.Map;

public class RequestParamValidator implements Validator<Map<String, String>> {
    @Override
    public void validate(Map<String, String> params) {
        LocalDateTime start = LocalDateTime.parse(params.get("start"), HitMapper.formatter);
        LocalDateTime end = LocalDateTime.parse(params.get("end"), HitMapper.formatter);
        if (start.isAfter(end)) {
            throw new ValidationException("Время начала должно быть раньше времени конца");
        }
        if (start.isAfter(LocalDateTime.now())) {
            throw new ValidationException("Время начала не может быть в будущем");
        }

    }
}
