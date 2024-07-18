package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.ModeratorCommentDto.CommentDto;
import ru.practicum.ewm.dto.ModeratorCommentDto.CommentDtoRequest;

public interface CommentService {
    CommentDto create(Long userId, Long eventId, CommentDtoRequest comment);

    CommentDto update(Long userId, Long commentId, CommentDtoRequest comment);

}
