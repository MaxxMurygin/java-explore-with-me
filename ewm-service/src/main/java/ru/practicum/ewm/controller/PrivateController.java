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
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.model.EventRequest;
import ru.practicum.ewm.service.EventService;
import ru.practicum.ewm.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class PrivateController {
    private final EventService eventService;
    private final RequestService requestService;

    @GetMapping("/{userId}/events")
    public List<EventDtoFull> getAllEventsByUser(@PathVariable(name = "userId") @Positive Long userId,
                                           @RequestParam(name = "from", defaultValue = "0") Integer from,
                                           @RequestParam(name = "size", defaultValue = "10") Integer size) {

        Pageable userPage = PageRequest.of(from / size, size, Sort.by("eventDate").ascending());
        return eventService.findAllByUser(userId, userPage);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventDtoFull getEventByUser(@PathVariable(name = "userId") @Positive Long userId,
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


    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getEventRequestsByUser(@PathVariable(name = "userId") @Positive Long userId,
                                                            @PathVariable(name = "eventId") @Positive Long eventId) {

        return requestService.getAllByInitiatorId(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequestsStatus(@PathVariable(name = "userId") @Positive Long userId,
                                                               @PathVariable(name = "eventId") @Positive Long eventId,
                                                               @Valid @RequestBody EventRequestStatusUpdateRequest request) {

        return requestService.updateStatus(userId, eventId, request);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createEventRequest(@PathVariable(name = "userId") @Positive Long userId,
                                                      @RequestParam(name = "eventId") Long eventId) {
        return requestService.create(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelEventRequest(@PathVariable(name = "userId") Long userid,
                                                      @PathVariable(name = "requestId") Long requestId) {
        return requestService.cancel(userid, requestId);
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getAllRequestsByUser(@PathVariable(name = "userId") @Positive Long userId) {

        return requestService.getAllByRequesterId(userId);
    }
}