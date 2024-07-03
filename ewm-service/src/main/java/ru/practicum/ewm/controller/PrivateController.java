package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.event.UpdateEventUserRequest;
import ru.practicum.ewm.service.EventService;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class PrivateController {
    private final EventService eventService;

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable(name = "userId") Long initiatorId,
                                    @Valid @RequestBody NewEventDto newEventDto) {
        return eventService.create(initiatorId, newEventDto);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable(name = "userId") Long initiatorId,
                                    @PathVariable(name = "eventId") Long eventId,
                                    @Valid @RequestBody UpdateEventUserRequest changedEventDto) {
        return eventService.update(initiatorId, eventId, changedEventDto);
    }
}