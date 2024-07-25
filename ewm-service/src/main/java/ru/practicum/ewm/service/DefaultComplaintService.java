package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.complaint.ComplaintAdminReview;
import ru.practicum.ewm.dto.complaint.ComplaintDto;
import ru.practicum.ewm.dto.complaint.ComplaintDtoRequest;
import ru.practicum.ewm.dto.complaint.ComplaintMapper;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.model.Comment;
import ru.practicum.ewm.model.Complaint;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model.enums.ComplaintStatus;
import ru.practicum.ewm.repository.CommentRepository;
import ru.practicum.ewm.repository.ComplaintRepository;
import ru.practicum.ewm.repository.UserRepository;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DefaultComplaintService implements ComplaintService {
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ComplaintRepository complaintRepository;
    private static final String ADMIN_DEFAULT_COMMENT_TEXT = "Содержание удалено модератором!";

    @Override
    @Transactional
    public ComplaintDto create(Long userId, Long commentId, ComplaintDtoRequest complaint) {
        User complainant = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class,
                        String.format(" with id=%d ", userId)));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(Comment.class,
                        String.format(" with id=%d ", commentId)));
        Complaint newComplaint = ComplaintMapper.fromRequest(comment, complainant, complaint);

        newComplaint.setCreatedOn(LocalDateTime.now());
        newComplaint.setStatus(ComplaintStatus.FILED);

        return ComplaintMapper.toComplaintDto(complaintRepository
                .save(newComplaint));
    }

    @Override
    @Transactional
    public ComplaintDto cancel(Long userId, Long complaintId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class,
                        String.format(" with id=%d ", userId)));
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new NotFoundException(Complaint.class,
                        String.format(" with id=%d ", complaintId)));

        if (!complaint.getComplainant().getId().equals(userId)) {
            throw new BadRequestException("Можно отменить только свою жалобу.");
        }
        complaint.setReviewedOn(LocalDateTime.now());
        complaint.setStatus(ComplaintStatus.CANCELED);

        return ComplaintMapper.toComplaintDto(complaintRepository
                .save(complaint));
    }

    @Override
    public List<ComplaintDto> getAll(ComplaintStatus[] statuses, Pageable pageable) {

        if (statuses == null) {
            return complaintRepository
                    .findAll(pageable)
                    .getContent()
                    .stream()
                    .map(ComplaintMapper::toComplaintDto)
                    .collect(Collectors.toList());
        }
        return complaintRepository
                .findAllByStatusIn(statuses, pageable)
                .stream()
                .map(ComplaintMapper::toComplaintDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ComplaintDto review(Long complaintId, ComplaintAdminReview adminReview) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new NotFoundException(Complaint.class,
                        String.format(" with id=%d ", complaintId)));
        Comment comment = commentRepository.findById(complaint.getComment().getId())
                .orElseThrow(() -> new NotFoundException(Comment.class,
                        String.format(" with id=%d ", complaint.getComment().getId())));
        String changedText = adminReview.getChangedCommentText();
        ComplaintStatus complaintStatus = complaint.getStatus();

        if (complaintStatus.equals(ComplaintStatus.REJECTED) ||
                complaintStatus.equals(ComplaintStatus.SATISFIED) ||
                complaintStatus.equals(ComplaintStatus.CANCELED)) {
                throw new BadRequestException("Нельзя рассмотреть уже рассмотренную или отмененную жалобу.");
        }

        switch (adminReview.getAction()) {
            case DELETE:
                commentRepository.deleteById(comment.getId());
                break;
            case MODIFY:
                if (changedText == null || changedText.isBlank()) {
                    comment.setText(ADMIN_DEFAULT_COMMENT_TEXT);
                } else {
                    comment.setText(changedText);
                }
                commentRepository.save(comment);
                break;
            case NO_ACTION:
        }
        switch (adminReview.getComplaintStatus()) {
            case REJECTED:
                complaint.setReviewedOn(LocalDateTime.now());
                complaint.setStatus(ComplaintStatus.REJECTED);
                break;
            case ON_REVIEW:
                complaint.setStatus(ComplaintStatus.ON_REVIEW);
                break;
            case SATISFIED:
                complaint.setReviewedOn(LocalDateTime.now());
                complaint.setStatus(ComplaintStatus.SATISFIED);
            default:
        }

        return ComplaintMapper.toComplaintDto(
                complaintRepository.save(complaint));
    }
}
