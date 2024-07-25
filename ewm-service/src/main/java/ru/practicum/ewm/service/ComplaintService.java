package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.complaint.ComplaintAdminReview;
import ru.practicum.ewm.dto.complaint.ComplaintDto;
import ru.practicum.ewm.dto.complaint.ComplaintDtoRequest;
import ru.practicum.ewm.model.enums.ComplaintStatus;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ComplaintService {

    ComplaintDto create(Long userId, Long commentId, ComplaintDtoRequest complaint);

    ComplaintDto cancel(Long userId, Long complaintId);

    List<ComplaintDto> getAll(ComplaintStatus[] statuses, Pageable pageable);

    ComplaintDto review(Long complaintId, ComplaintAdminReview adminReview);

}
