package ru.practicum.ewm.validator;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.model.Event;

@Component
public class EventValidator implements Validator<Event> {
    @Override
    public void validate(Event event) {
        if (event.getEventDate().isBefore(event.getCreatedOn().plusHours(2))) {
            throw new ValidationException(Event.class,
                    "Событие должно начаться не ранее, чем через 2 часа после создания");
        }
    }
}
