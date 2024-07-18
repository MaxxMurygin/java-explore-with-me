package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.ModeratorCommentDto.CommentDto;
import ru.practicum.ewm.dto.ModeratorCommentDto.CommentDtoRequest;
import ru.practicum.ewm.dto.ModeratorCommentDto.CommentMapper;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.model.Comment;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.CommentRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DefaultCommentService implements CommentService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;
    @Override
    @Transactional
    public CommentDto create(Long userId, Long eventId, CommentDtoRequest comment) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class,
                        String.format(" with id=%d ", userId)));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(Event.class,
                        String.format(" with id=%d ", eventId)));

        Comment newComment = CommentMapper.fromRequest(event, author, comment);

        newComment.setCreatedOn(LocalDateTime.now());

        return CommentMapper.toCommentDto(
                commentRepository.save(newComment));
    }

    @Override
    @Transactional
    public CommentDto update(Long userId, Long commentId, CommentDtoRequest comment) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class,
                        String.format(" with id=%d ", userId)));
        Comment stored = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(Comment.class,
                        String.format(" with id=%d ", commentId)));

        if (!stored.getAuthor().getId().equals(author.getId())) {
            throw new BadRequestException("Можно редактировать только свои комментарии.");
        }
        stored.setEditedOn(LocalDateTime.now());

        return CommentMapper.toCommentDto(
                commentRepository.save(stored));
    }
}
