package ru.practicum.ewm.dto.complaint;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class ComplaintDtoRequest {
    @Size(min = 2, max = 1000, message = "Содержание жалобы должно быть в диапазоне 2-1000 символов")
    private String text;
}
