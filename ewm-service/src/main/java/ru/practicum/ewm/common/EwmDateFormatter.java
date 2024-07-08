package ru.practicum.ewm.common;

import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public final class EwmDateFormatter {

    public static DateTimeFormatter getFormatter() {

        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }
}