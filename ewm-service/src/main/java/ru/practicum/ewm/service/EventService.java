package ru.practicum.ewm.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.dto.event.*;

import java.util.List;

public interface EventService {
    EventDtoFull create(Long initiatorId, NewEventDto newEventDto);

    EventDtoFull update(Long initiatorId, Long eventId, UpdateEventUserRequest changedEventDto);

    EventDtoFull update(Long eventId, UpdateEventAdminRequest changedEventDto);

    EventDtoFull findByUser(Long userId, Long eventId);

    EventDtoFull findById(Long eventId);

    List<EventDtoFull> findAllByUser(Long userId, Pageable pageable);

    List<EventDtoShort> findAllByIds(List<Long> eventsIds);

    List<EventDtoFull> findAllByParams(Long[] usersIds,
                                       String[] states,
                                       Long[] categoriesIds,
                                       String start,
                                       String end,
                                       Pageable pageable);

    List<EventDtoShort> findAllByParams(String text,
                                        Long[] categoriesIds,
                                        Boolean paid,
                                        String start,
                                        String end,
                                        Boolean onlyAvailable,
                                        Pageable pageable);
}
