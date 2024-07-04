package ru.practicum.ewm.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.event.UpdateEventUserRequest;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface EventService {
    EventFullDto create(Long initiatorId, NewEventDto newEventDto);

    EventFullDto update(Long initiatorId, Long eventId, UpdateEventUserRequest changedEventDto);

    EventFullDto findByUser(Long userId, Long eventId);


    List<EventFullDto> findAllByUser(Long userId, Pageable pageable);

    List<EventFullDto> findAllByParams(Map<String, Object> params, Pageable pageable);
}
