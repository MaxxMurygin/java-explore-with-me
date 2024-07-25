package ru.practicum.ewm.dto.comment;

import ru.practicum.ewm.common.EwmDateFormatter;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.Comment;
import ru.practicum.ewm.model.User;

import java.time.format.DateTimeFormatter;

public class CommentMapper {
    private static final DateTimeFormatter formatter = EwmDateFormatter.getFormatter();

    public static Comment fromRequest(Event event, User author, CommentDtoRequest comment) {
        return Comment.builder()
                .text(comment.getText())
                .eventId(event.getId())
                .author(author)
                .build();
    }

    public static CommentDto toCommentDto(Comment comment) {
        CommentDto result = CommentDto.builder()
                .id(comment.getId())
                .authorName(comment.getAuthor().getName())
                .text(comment.getText())
                .createdOn(comment.getCreatedOn().format(formatter))
                .build();

        if (comment.getEditedOn() != null) {
            result.setEditedOn(comment.getEditedOn().format(formatter));
        }
        return result;
    }
}
