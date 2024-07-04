package ru.practicum.ewm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
public class Location {
    @NotNull
    @Range(min = -90, max =  90, message = "Долгота в евклидовом пространстве находится в диапазоне [-90:90]")
    private Float lat;
    @NotNull
    @Range(min = -180, max =  180, message = "Широта в евклидовом пространстве находится в диапазоне [-180:180]")
    private Float lon;
}
