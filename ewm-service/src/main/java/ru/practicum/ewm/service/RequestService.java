package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    ParticipationRequestDto create(Long userId, Long eventId);

    ParticipationRequestDto cancel(Long userId, Long requestId);

    List<ParticipationRequestDto> getAllByRequesterId(Long requesterId);

    List<ParticipationRequestDto> getAllByInitiatorId(Long initiatorId, Long eventId);

    EventRequestStatusUpdateResult updateStatus(Long initiatorId, Long eventId,
                                                EventRequestStatusUpdateRequest request);


}
