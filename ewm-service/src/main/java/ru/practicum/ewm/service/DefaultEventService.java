package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.common.EwmDateFormatter;
import ru.practicum.ewm.dto.category.CategoryMapper;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventMapper;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.event.UpdateEventUserRequest;
import ru.practicum.ewm.dto.user.UserMapper;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.enums.EventState;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.validator.EventValidator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultEventService implements EventService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final DateTimeFormatter formatter = EwmDateFormatter.getFormatter();
    @Autowired
    private EventValidator validator;
    @Override
    public EventFullDto create(Long initiatorId, NewEventDto newEventDto) {
        Long categoryId = newEventDto.getCategory();
        User initiator = userRepository.findById(initiatorId).
                orElseThrow(() -> new NotFoundException(User.class,
                                                            String.format(" with id=%d ", initiatorId)));
        Category category = categoryRepository.findById(categoryId ).
                orElseThrow(() -> new NotFoundException(Category.class,
                                                            String.format(" with id=%d ", categoryId)));

        Event newEvent = EventMapper.fromNewEventDto(initiatorId, newEventDto);
        newEvent.setCreatedOn(LocalDateTime.now());
        newEvent.setState(EventState.PUBLISHED);
        newEvent.setPublishedOn(LocalDateTime.now());
        newEvent.setConfirmedRequests(0L);
        newEvent.setViews(0L);
        if (newEvent.getPaid() == null) {
            newEvent.setPaid(false);
        }
        if (newEvent.getParticipantLimit() == null) {
            newEvent.setParticipantLimit(0);
        }
        if (newEvent.getRequestModeration() == null) {
            newEvent.setRequestModeration(true);
        }

        validator.validate(newEvent);
        return EventMapper.toEventFullDto(eventRepository.save(newEvent),
                CategoryMapper.toDto(category),
                UserMapper.toDtoShort(initiator));
    }

    @Override
    public EventFullDto update(Long initiatorId, Long eventId, UpdateEventUserRequest changedEventDto) {

        User initiator = userRepository.findById(initiatorId).
                orElseThrow(() -> new NotFoundException(User.class,
                        String.format(" with id=%d ", initiatorId)));

        Event stored = eventRepository.findById(eventId).
                orElseThrow(() -> new NotFoundException(Event.class,
                        String.format(" with id=%d ", eventId)));
        Long categoryId = stored.getCategoryId();
        Category category = categoryRepository.findById(categoryId).
                orElseThrow(() -> new NotFoundException(Category.class,
                        String.format(" with id=%d ", categoryId)));

        if (stored.getState().equals(EventState.PUBLISHED)) {
            throw new ValidationException(Event.class, "Нельзя изменить опубликованные события");
        }
        if (changedEventDto.getAnnotation() != null) {
            stored.setAnnotation(changedEventDto.getAnnotation());
        }
        if (changedEventDto.getCategory() != null) {
            Long changedCategoryId = changedEventDto.getCategory();
            category = categoryRepository.findById(changedCategoryId).
                    orElseThrow(() -> new NotFoundException(Category.class,
                            String.format(" with id=%d ", changedCategoryId)));

            stored.setCategoryId(changedCategoryId);
        }
        if (changedEventDto.getDescription() != null) {
            stored.setDescription(changedEventDto.getDescription());
        }
        if (changedEventDto.getEventDate() != null) {
            stored.setEventDate(LocalDateTime.parse(changedEventDto.getEventDate(), formatter));
        }
        if (changedEventDto.getLocation() != null) {
            stored.setLocationLat(changedEventDto.getLocation().getLat());
            stored.setLocationLon(changedEventDto.getLocation().getLon());
        }
        if (changedEventDto.getPaid() != null) {
            stored.setPaid(changedEventDto.getPaid());
        }
        if (changedEventDto.getParticipantLimit() != null) {
            stored.setParticipantLimit(changedEventDto.getParticipantLimit());
        }
        if (changedEventDto.getRequestModeration() != null) {
            stored.setRequestModeration(changedEventDto.getRequestModeration());
        }
        if (changedEventDto.getTitle() != null) {
            stored.setTitle(changedEventDto.getTitle());
        }
        validator.validate(stored);
        return EventMapper.toEventFullDto(eventRepository.save(stored),
                CategoryMapper.toDto(category),
                UserMapper.toDtoShort(initiator));
    }
}
