package ru.practicum.ewm.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public final class EwmDateFormatter {
    private static String STRING_FORMAT;

    @Value("${ewm.date.format}")
    public void setStaticFormat(String format) {
        STRING_FORMAT = format;
    }

    public static DateTimeFormatter getFormatter() {
        return DateTimeFormatter.ofPattern(STRING_FORMAT);
    }
}
