package ru.practicum.ewm.dto.event;

import lombok.Data;
import ru.practicum.ewm.model.Location;

import javax.validation.constraints.*;

@Data
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000, message = "Length of event annotation must be in range 20-2000")
    private String annotation;
    @NotNull
    private Long category;
    @NotBlank
    @Size(min = 20, max = 7000, message = "Length of event description must be in range 20-7000")
    private String description;
    @NotBlank
    private String eventDate;
    @NotNull
    private Location location;
    private Boolean paid;
    @PositiveOrZero
    private Integer participantLimit;
    private Boolean requestModeration;
    @NotBlank
    @Size(min = 3, max = 120, message = "Length of event title must be in range 3-120")
    private String title;
}
