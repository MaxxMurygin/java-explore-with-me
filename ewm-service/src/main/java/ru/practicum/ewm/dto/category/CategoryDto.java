package ru.practicum.ewm.dto.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class CategoryDto {
    private Long id;
    @NotBlank(message = "[name] cannot be blank")
    @Size(min = 2, max = 50, message = "Length of Category name must be in range 2-50 symbols")
    private String name;
}
