package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.dto.request.ParticipationRequestMapper;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventRequest;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model.enums.EventRequestStatus;
import ru.practicum.ewm.model.enums.EventState;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.EventRequestRepository;
import ru.practicum.ewm.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DefaultRequestService implements RequestService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final EventRequestRepository requestRepository;

    @Override
    @Transactional
    public ParticipationRequestDto create(Long userId, Long eventId) {
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class,
                        String.format(" with id=%d ", userId)));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(Event.class,
                        String.format(" with id=%d ", eventId)));

        if (requestRepository.findByRequesterIdAndEventId(userId, eventId) != null) {
            throw new ValidationException(EventRequest.class, "Заявка на участие уже существует.");
        }
        if (userId.equals(event.getInitiator().getId())) {
            throw new ValidationException(EventRequest.class, "Нельзя подавать заявку на участие в своем мероприятии.");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ValidationException(EventRequest.class, "Это мероприятие еще не опубликовано.");
        }
        if ((event.getParticipantLimit() != 0) &&
                (event.getParticipantLimit() <= event.getConfirmedRequests())) {
            throw new ValidationException(EventRequest.class, "Достигнут лимит участников.");
        }
        EventRequest request = EventRequest.builder()
                .created(LocalDateTime.now())
                .requester(requester)
                .event(event)
                .status(EventRequestStatus.PENDING)
                .build();
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(EventRequestStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }
        log.info("Create request: " + request);
        log.info("UserId: " + userId + " EventId: " + eventId);
        return ParticipationRequestMapper.toDto(requestRepository.save(request));
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancel(Long userId, Long requestId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class,
                        String.format(" with id=%d ", userId)));
        EventRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(EventRequest.class,
                        String.format(" with id=%d ", requestId)));

        request.setStatus(EventRequestStatus.CANCELED);
        requestRepository.save(request);

        return ParticipationRequestMapper.toDto(request);
    }

    @Override
    public List<ParticipationRequestDto> getAllByRequesterId(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class,
                        String.format(" with id=%d ", userId)));

        return requestRepository.findAllByRequesterId(userId)
                .stream()
                .map(ParticipationRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParticipationRequestDto> getAllByInitiatorId(Long initiatorId, Long eventId) {
        userRepository.findById(initiatorId)
                .orElseThrow(() -> new NotFoundException(User.class,
                        String.format(" with id=%d ", initiatorId)));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(Event.class,
                        String.format(" with id=%d ", eventId)));

        if (!event.getInitiator().getId().equals(initiatorId)) {
            throw new ValidationException(EventRequest.class, "Нельзя просматривать запросы не ко своим событиям.");
        }

        return requestRepository.findAllByEventId(eventId)
                .stream()
                .map(ParticipationRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateStatus(Long initiatorId, Long eventId,
                                                       EventRequestStatusUpdateRequest request) {
        List<EventRequest> eventRequests;
        List<Long> requestsIds = request.getRequestsIds();


        log.info("Update request service: " + request + " initiatorId: " + initiatorId + " eventId:" + eventId);

        if (requestsIds == null) {
            eventRequests = requestRepository.findAllByEventId(eventId);
        } else {
            eventRequests = requestRepository.findAllById(requestsIds);
        }
        EventRequestStatus action = request.getStatus();

        userRepository.findById(initiatorId)
                .orElseThrow(() -> new NotFoundException(User.class,
                        String.format(" with id=%d ", initiatorId)));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(Event.class,
                        String.format(" with id=%d ", eventId)));
        Integer limit = event.getParticipantLimit();
        Long confirmed = event.getConfirmedRequests();

        for (EventRequest er: eventRequests) {
            if (confirmed <= limit) {
                if (er.getStatus().equals(EventRequestStatus.PENDING)) {
                    er.setStatus(action);
                    if (action.equals(EventRequestStatus.CONFIRMED)) {
                        ++confirmed;
                    }
                } else {
                    throw new ValidationException(
                            EventRequest.class, "Можно изменить только ожидающую заявку.");
                }
            } else {
                throw new ValidationException(
                        EventRequest.class, "Достигнут лимит участников.");
            }
        }

        event.setConfirmedRequests(confirmed);
        log.info("Updated event: \n" + event);
        eventRepository.save(event);

        requestRepository.saveAll(eventRequests);

        return ParticipationRequestMapper.toResult(eventRequests);
    }
}
