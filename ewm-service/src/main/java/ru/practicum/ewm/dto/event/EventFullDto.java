package ru.practicum.ewm.dto.event;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.user.UserDtoShort;
import ru.practicum.ewm.model.enums.EventState;
import ru.practicum.ewm.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class EventFullDto {
    @NotBlank
    @Size(min = 20, max = 2000, message = "Length of event annotation must be in range 20-2000")
    private String annotation;
    @NotNull
    private CategoryDto category;
    private Long confirmedRequests;
    private String createdOn;
    @NotBlank
    @Size(min = 20, max = 7000, message = "Length of event description must be in range 20-7000")
    private String description;
    @NotBlank
    private String eventDate;
    private Long id;
    @NotNull
    private UserDtoShort initiator;
    @NotNull
    private Location location;
    @NotNull
    private Boolean paid;
    private Integer participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private EventState state;
    @NotBlank
    @Size(min = 3, max = 120, message = "Length of event title must be in range 3-120")
    private String title;
    private Long views;
}
