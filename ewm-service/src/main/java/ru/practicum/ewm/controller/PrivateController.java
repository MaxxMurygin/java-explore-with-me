package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.comment.CommentDto;
import ru.practicum.ewm.dto.comment.CommentDtoRequest;
import ru.practicum.ewm.dto.complaint.ComplaintDto;
import ru.practicum.ewm.dto.complaint.ComplaintDtoRequest;
import ru.practicum.ewm.dto.event.EventDtoFull;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.event.UpdateEventUserRequest;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.service.CommentService;
import ru.practicum.ewm.service.ComplaintService;
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
    private final CommentService commentService;
    private final ComplaintService complaintService;

    @GetMapping("/{userId}/events")
    public List<EventDtoFull> getAllEventsByUser(
            @PathVariable(name = "userId") @Positive Long userId,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {

        Pageable userPage = PageRequest.of(from / size, size, Sort.by("eventDate").ascending());

        log.info("Запрос на поиск событий пользователя: userId={}, from={}, size={}", userId, from, size);
        return eventService.findAllByUser(userId, userPage);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventDtoFull getEventByUser(
            @PathVariable(name = "userId") @Positive Long userId,
            @PathVariable(name = "eventId") @Positive Long eventId) {

        log.info("Запрос на поиск события пользователя: userId={}, eventId={}", userId, eventId);
        return eventService.findByUser(userId, eventId);
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventDtoFull createEvent(
            @PathVariable(name = "userId") @Positive Long initiatorId,
            @Valid @RequestBody NewEventDto newEventDto) {

        log.info("Запрос на создание события пользователем: userId={}", initiatorId);
        log.debug("newEventDto: " + newEventDto);
        return eventService.create(initiatorId, newEventDto);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventDtoFull updateEvent(
            @PathVariable(name = "userId") Long initiatorId,
            @PathVariable(name = "eventId") Long eventId,
            @Valid @RequestBody UpdateEventUserRequest changedEventDto) {

        log.info("Запрос на изменение события пользователем: userId={}, eventId={}", initiatorId, eventId);
        log.debug("changedEventDto: " + changedEventDto);
        return eventService.update(initiatorId, eventId, changedEventDto);
    }


    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getEventRequestsByUser(
            @PathVariable(name = "userId") @Positive Long userId,
            @PathVariable(name = "eventId") @Positive Long eventId) {

        log.info("Запрос на вывод запросов на событие пользователя: userId={}, eventId={}", userId, eventId);
        return requestService.getAllByInitiatorId(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequestsStatus(
            @PathVariable(name = "userId") @Positive Long userId,
            @PathVariable(name = "eventId") @Positive Long eventId,
            @Valid @RequestBody EventRequestStatusUpdateRequest request) {

        log.info("Запрос на принятие/отклонение запросов на событие пользователя: userId={}, eventId={}",
                userId,eventId);
        log.debug("request={}", request);
        return requestService.updateStatus(userId, eventId, request);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createEventRequest(
            @PathVariable(name = "userId") @Positive Long userId,
            @RequestParam(name = "eventId") Long eventId) {

        log.info("Запрос на участие в событии пользователя: userId={}, eventId={}", userId, eventId);
        return requestService.create(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelEventRequest(
            @PathVariable(name = "userId") Long userId,
            @PathVariable(name = "requestId") Long requestId) {

        log.info("Отмена запроса на участие в событии пользователя: userId={}, requestId={}", userId, requestId);
        return requestService.cancel(userId, requestId);
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getAllRequestsByUser(
            @PathVariable(name = "userId") @Positive Long userId) {

        log.info("Просмотр своих запросов на участие в событии пользователя: userId={}", userId);
        return requestService.getAllByRequesterId(userId);
    }

    @PostMapping("/{userId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(
            @PathVariable(name = "userId") @Positive Long userId,
            @RequestParam(name = "eventId") Long eventId,
            @Valid @RequestBody CommentDtoRequest comment) {

        log.info("Создание комментария пользователем: userId={}, eventId={}", userId, eventId);
        return commentService.create(userId, eventId, comment);
    }

    @PatchMapping("/{userId}/comments/{commentId}")
    public CommentDto updateComment(
            @PathVariable(name = "userId") @Positive Long userId,
            @PathVariable(name = "commentId") @Positive Long commentId,
            @Valid @RequestBody CommentDtoRequest comment) {

        log.info("Редактирование комментария пользователем: userId={}, commentId={}", userId, commentId);
        return commentService.update(userId, commentId, comment);
    }

    @DeleteMapping("/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(
            @PathVariable(name = "userId") @Positive Long userId,
            @PathVariable(name = "commentId") @Positive Long commentId) {

        log.info("Удаление комментария пользователем: userId={}, commentId={}", userId, commentId);
        commentService.delete(userId, commentId);
    }

    @PostMapping("/{userId}/comments/{commentId}/complaints")
    @ResponseStatus(HttpStatus.CREATED)
    public ComplaintDto createComplaint(
            @PathVariable(name = "userId") @Positive Long userId,
            @PathVariable(name = "commentId") @Positive Long commentId,
            @Valid @RequestBody ComplaintDtoRequest complaint) {

        log.info("Создание жалобы на комментарий пользователем: userId={}, commentId={}", userId, commentId);
        return complaintService.create(userId, commentId, complaint);
    }

    @PatchMapping("/{userId}/complaints/{complaintId}/cancel")
    public ComplaintDto createComplaint(
            @PathVariable(name = "userId") @Positive Long userId,
            @PathVariable(name = "complaintId") @Positive Long complaintId) {

        log.info("Отмена жалобы на комментарий пользователем: userId={}, complaintId={}", userId, complaintId);
        return complaintService.cancel(userId, complaintId);
    }
}