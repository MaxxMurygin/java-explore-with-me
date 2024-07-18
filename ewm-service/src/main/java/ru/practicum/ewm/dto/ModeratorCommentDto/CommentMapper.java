package ru.practicum.ewm.dto.ModeratorCommentDto;

import ru.practicum.ewm.common.EwmDateFormatter;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.Comment;
import ru.practicum.ewm.model.User;

import java.time.format.DateTimeFormatter;

public class CommentMapper {
    private static final DateTimeFormatter formatter = EwmDateFormatter.getFormatter();

    public static Comment fromRequest(Event event, User author, CommentDtoRequest request) {
        return Comment.builder()
                .text(request.getDescription())
                .event(event)
                .author(author)
                .build();
    }

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .authorName(comment.getAuthor().getName())
                .text(comment.getText())
                .createdOn(comment.getCreatedOn())
                .editedOn(comment.getEditedOn())
                .build();
    }
}
