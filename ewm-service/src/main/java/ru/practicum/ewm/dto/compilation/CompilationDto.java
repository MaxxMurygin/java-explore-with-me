package ru.practicum.ewm.dto.compilation;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
public class CompilationDto {
    private List<Long> events;
    @NotNull
    private Long id;
    @NotNull
    private Boolean pinned;
    @NotBlank
    @Size(min = 1, max = 50, message = "Заголовок должен быть в диапазоне 2-50 символов")
    private String title;
}
