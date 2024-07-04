package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.event.EventDtoFull;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.event.UpdateEventUserRequest;
import ru.practicum.ewm.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class PrivateController {
    private final EventService eventService;

    @GetMapping("/{userId}/events")
    public List<EventDtoFull> getAllByUser(@PathVariable(name = "userId") @Positive Long userId,
                                           @RequestParam(name = "from", defaultValue = "0") Integer from,
                                           @RequestParam(name = "size", defaultValue = "10") Integer size) {

        Pageable userPage = PageRequest.of(from / size, size, Sort.by("eventDate").ascending());
        return eventService.findAllByUser(userId, userPage);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventDtoFull getByUser(@PathVariable(name = "userId") @Positive Long userId,
                                  @PathVariable(name = "eventId") @Positive Long eventId) {

        return eventService.findByUser(userId, eventId);
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventDtoFull createEvent(@PathVariable(name = "userId") @Positive Long initiatorId,
                                    @Valid @RequestBody NewEventDto newEventDto) {
        return eventService.create(initiatorId, newEventDto);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventDtoFull updateEvent(@PathVariable(name = "userId") Long initiatorId,
                                    @PathVariable(name = "eventId") Long eventId,
                                    @Valid @RequestBody UpdateEventUserRequest changedEventDto) {
        return eventService.update(initiatorId, eventId, changedEventDto);
    }
}