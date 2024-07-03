package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.event.UpdateEventUserRequest;

public interface EventService {
    EventFullDto create(Long initiatorId, NewEventDto newEventDto);
    EventFullDto update(Long initiatorId, Long eventId, UpdateEventUserRequest changedEventDto);
}
