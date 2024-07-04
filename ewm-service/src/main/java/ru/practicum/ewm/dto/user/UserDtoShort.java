package ru.practicum.ewm.dto.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class UserDtoShort {
    @NotNull
    private Long id;
    @NotBlank
    @Size(min = 2, max = 50)
    private String name;
}
