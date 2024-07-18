package ru.practicum.ewm.dto.ModeratorCommentDto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {
    private Long id;
    @Size(min = 2, max = 2000, message = "Содержание должно быть в диапазоне 2-2000 символов")
    private String text;
    @NotBlank
    @Size(min = 2, max = 250, message = "Имя автора должно быть в диапазоне 2-250 символов")
    private String authorName;
    @NotNull
    private LocalDateTime createdOn;
    @NotNull
    private LocalDateTime editedOn;
}
