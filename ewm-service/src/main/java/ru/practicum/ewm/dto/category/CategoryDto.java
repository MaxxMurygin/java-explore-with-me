package ru.practicum.ewm.dto.category;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class CategoryDto {
    private Long id;
    @NotBlank
    @Size(min = 2, max = 50, message = "Имя должно быть в диапазоне 2-50 символов")
    private String name;
}
