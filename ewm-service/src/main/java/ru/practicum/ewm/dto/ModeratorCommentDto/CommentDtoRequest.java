package ru.practicum.ewm.dto.ModeratorCommentDto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class CommentDtoRequest {
    @Size(min = 2, max = 2000, message = "Содержание должно быть в диапазоне 2-2000 символов")
    private String description;
}
