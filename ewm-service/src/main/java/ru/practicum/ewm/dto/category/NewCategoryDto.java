package ru.practicum.ewm.dto.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
public class NewCategoryDto {
    @NotBlank(message = "[name] cannot be blank")
    @Size(min = 2, max = 50, message = "Length of Category name must be in range 2-50 symbols")
    private String name;
}
