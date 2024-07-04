package ru.practicum.ewm.dto.event;

import lombok.Data;
import ru.practicum.ewm.model.Location;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
public class UpdateEventRequest {
    @Size(min = 20, max = 2000, message = "Length of event annotation must be in range 20-2000")
    protected String annotation;
    protected Long category;
    @Size(min = 20, max = 7000, message = "Length of event description must be in range 20-7000")
    protected String description;
    protected String eventDate;
    protected Location location;
    protected Boolean paid;
    @PositiveOrZero
    protected Integer participantLimit;
    protected Boolean requestModeration;
    @Size(min = 3, max = 120, message = "Length of event title must be in range 3-120")
    protected String title;
}
