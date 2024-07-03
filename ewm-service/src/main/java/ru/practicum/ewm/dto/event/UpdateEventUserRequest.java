package ru.practicum.ewm.dto.event;

import lombok.Data;
import ru.practicum.ewm.model.Location;
import ru.practicum.ewm.model.enums.EventState;
import ru.practicum.ewm.model.enums.EventStateAction;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
public class UpdateEventUserRequest {
    @Size(min = 20, max = 2000, message = "Length of event annotation must be in range 20-2000")
    private String annotation;
    private Long category;
    @Size(min = 20, max = 7000, message = "Length of event description must be in range 20-7000")
    private String description;
    private String eventDate;
    private Location location;
    private Boolean paid;
    @PositiveOrZero
    private Integer participantLimit;
    private Boolean requestModeration;
    private EventStateAction stateAction;
    @Size(min = 3, max = 120, message = "Length of event title must be in range 3-120")
    private String title;
}
