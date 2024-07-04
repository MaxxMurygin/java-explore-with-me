package ru.practicum.ewm.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.event.UpdateEventAdminRequest;
import ru.practicum.ewm.dto.event.UpdateEventUserRequest;

import java.util.List;

public interface EventService {
    EventFullDto create(Long initiatorId, NewEventDto newEventDto);

    EventFullDto update(Long initiatorId, Long eventId, UpdateEventUserRequest changedEventDto);

    EventFullDto update(Long eventId, UpdateEventAdminRequest changedEventDto);

    EventFullDto findByUser(Long userId, Long eventId);


    List<EventFullDto> findAllByUser(Long userId, Pageable pageable);

    List<EventFullDto> findAllByParams(Long[] usersIds,
                                       String[] states,
                                       Long[] categoriesIds,
                                       String start,
                                       String end,
                                       Pageable pageable);
}
