package ru.practicum.ewm.dto.event;

import lombok.Builder;
import lombok.Data;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.user.UserDtoShort;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class EventDtoShort {
    @NotBlank
    @Size(min = 20, max = 2000, message = "Length of event annotation must be in range 20-2000")
    private String annotation;
    @NotNull
    private CategoryDto category;
    private Long confirmedRequests;
    @NotBlank
    private String eventDate;
    private Long id;
    @NotNull
    private UserDtoShort initiator;
    private Boolean paid;
    @NotBlank
    @Size(min = 3, max = 120, message = "Length of event title must be in range 3-120")
    private String title;
    private Long views;
}
