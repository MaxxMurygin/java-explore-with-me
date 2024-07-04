package ru.practicum.ewm.dto.event;

import lombok.Data;
import ru.practicum.ewm.model.Location;

import javax.validation.constraints.*;

@Data
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000, message = "Аннотация должна быть в диапазоне 20-2000 символов")
    private String annotation;
    @NotNull
    private Long category;
    @NotBlank
    @Size(min = 20, max = 7000, message = "Описание должно быть в диапазоне 20-7000 символов")
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
    @Size(min = 3, max = 120, message = "Заголовок должен быть в диапазоне 3-120 символов")
    private String title;
}
