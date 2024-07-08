package ru.practicum.ewm.dto.compilation;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

@Data
public class UpdateCompilationRequest {
    private List<Long> events;
    private Boolean pinned;
    @Size(min = 1, max = 50, message = "Заголовок должен быть в диапазоне 1-50 символов")
    private String title;
}
