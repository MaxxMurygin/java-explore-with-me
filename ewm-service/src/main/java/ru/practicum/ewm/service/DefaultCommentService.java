package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.comment.CommentDto;
import ru.practicum.ewm.dto.comment.CommentDtoRequest;
import ru.practicum.ewm.dto.comment.CommentMapper;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.model.Comment;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.repository.CommentRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        stored.setText(comment.getText());
        stored.setEditedOn(LocalDateTime.now());

        return CommentMapper.toCommentDto(
                commentRepository.save(stored));
    }

    @Override
    @Transactional
    public void delete(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(Comment.class,
                        String.format(" with id=%d ", commentId)));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class,
                        String.format(" with id=%d ", userId)));

        if (!comment.getAuthor().getId().equals(user.getId())) {
            throw new BadRequestException("Можно удалить только свои комментарии.");
        }

        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentDto> findAllByEvent(Long eventId, Pageable pageable) {
        eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(Event.class,
                        String.format(" with id=%d ", eventId)));

        return commentRepository
                .findAllByEventId(eventId, pageable)
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }
}
