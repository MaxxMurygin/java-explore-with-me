package ru.practicum.ewm.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.dto.comment.CommentDto;
import ru.practicum.ewm.dto.comment.CommentDtoRequest;

import java.util.List;

public interface CommentService {
    CommentDto create(Long userId, Long eventId, CommentDtoRequest comment);

    CommentDto update(Long userId, Long commentId, CommentDtoRequest comment);

    void delete(Long userId, Long commentId);

    List<CommentDto> findAllByEvent(Long eventId, Pageable pageable);
}
