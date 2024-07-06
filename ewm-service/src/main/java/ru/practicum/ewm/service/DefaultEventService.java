package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.common.EwmDateFormatter;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.category.CategoryMapper;
import ru.practicum.ewm.dto.event.*;
import ru.practicum.ewm.dto.user.UserDtoShort;
import ru.practicum.ewm.dto.user.UserMapper;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model.enums.EventState;
import ru.practicum.ewm.model.enums.EventStateAdminAction;
import ru.practicum.ewm.model.enums.EventStateUserAction;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.UserRepository;

import javax.persistence.criteria.Predicate;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DefaultEventService implements EventService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final DateTimeFormatter formatter = EwmDateFormatter.getFormatter();


    @Override
    @Transactional
    public EventDtoFull create(Long initiatorId, NewEventDto newEventDto) {
        Long categoryId = newEventDto.getCategory();
        User initiator = userRepository.findById(initiatorId)
                .orElseThrow(() -> new NotFoundException(User.class,
                                                            String.format(" with id=%d ", initiatorId)));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(Category.class,
                                                            String.format(" with id=%d ", categoryId)));

        Event newEvent = EventMapper.fromNewEventDto(initiator, category, newEventDto);
        newEvent.setCreatedOn(LocalDateTime.now());
        newEvent.setState(EventState.PENDING);
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
        if (newEvent.getEventDate().isBefore(newEvent.getCreatedOn().plusHours(2))) {
            throw new BadRequestException("Событие должно начаться не ранее, чем через 2 часа после создания");
        }
        return EventMapper.toEventDtoFull(eventRepository.save(newEvent));
    }

    @Override
    @Transactional
    public EventDtoFull update(Long initiatorId, Long eventId, UpdateEventUserRequest changedEventDto) {
        User initiator = userRepository.findById(initiatorId)
                .orElseThrow(() -> new NotFoundException(User.class,
                        String.format(" with id=%d ", initiatorId)));
        Event stored = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(Event.class,
                        String.format(" with id=%d ", eventId)));

        if (stored.getState().equals(EventState.PUBLISHED)) {
            throw new ValidationException(Event.class, "Нельзя изменить опубликованные события");
        }
        if (changedEventDto.getAnnotation() != null) {
            stored.setAnnotation(changedEventDto.getAnnotation());
        }
        if (changedEventDto.getCategory() != null) {
            Long changedCategoryId = changedEventDto.getCategory();
            Category category = categoryRepository.findById(changedCategoryId)
                    .orElseThrow(() -> new NotFoundException(Category.class,
                            String.format(" with id=%d ", changedCategoryId)));

            stored.setCategory(category);
        }
        if (changedEventDto.getDescription() != null) {
            stored.setDescription(changedEventDto.getDescription());
        }
        if (changedEventDto.getEventDate() != null) {
            stored.setEventDate(LocalDateTime.parse(changedEventDto.getEventDate(), formatter));
        }
        if (stored.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Нельзя редактировать событие, менее чем за 2 часа до его начала");
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
        if (changedEventDto.getStateAction() != null) {
            if (changedEventDto.getStateAction().equals(EventStateUserAction.CANCEL_REVIEW)) {
                stored.setState(EventState.CANCELED);
            } else {
                stored.setState(EventState.PENDING);
            }
        }

        return EventMapper.toEventDtoFull(eventRepository.save(stored));
    }

    @Override
    @Transactional
    public EventDtoFull update(Long eventId, UpdateEventAdminRequest changedEventDto) {

        Event stored = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(Event.class,
                        String.format(" with id=%d ", eventId)));

        if (stored.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ValidationException(Event.class,
                    "Нельзя изменять событие, менее чем за 1 час до его начала");
        }
        stored.setPublishedOn(LocalDateTime.now());
        if (changedEventDto.getStateAction() != null) {
            if (changedEventDto.getStateAction().equals(EventStateAdminAction.PUBLISH_EVENT)) {
                if (!stored.getState().equals(EventState.PENDING)) {
                    throw new ValidationException(Event.class,
                            "Событие можно публиковать, только если оно в состоянии ожидания публикации.");
                }
                stored.setState(EventState.PUBLISHED);
            } else {
                if (stored.getState().equals(EventState.PUBLISHED)) {
                    throw new ValidationException(Event.class,
                            "Событие можно отклонить, только если оно еще не опубликовано.");
                }
                stored.setState(EventState.CANCELED);
            }
        }
        if (changedEventDto.getDescription() != null) {
            stored.setDescription(changedEventDto.getDescription());
        }
        if (changedEventDto.getEventDate() != null) {
            LocalDateTime eventDate = LocalDateTime.parse(changedEventDto.getEventDate(), formatter);
            if (eventDate.isBefore(LocalDateTime.now())) {
                throw new BadRequestException("Дата начала не может быть в прошлом.");
            }
            stored.setEventDate(eventDate);
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

        return EventMapper.toEventDtoFull(eventRepository.save(stored));
    }

    @Override
    public List<EventDtoFull> findAllByUser(Long userId, Pageable pageable) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class,
                        String.format(" with id=%d ", userId)));
        List<Event> eventList = eventRepository.findAllByInitiatorId(userId,pageable);

        return eventList.stream()
                .map(EventMapper::toEventDtoFull)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDtoShort> findAllByIds(List<Long> eventsIds) {
        List<Event> eventList = eventRepository.findByIdIn(eventsIds);

        return eventList.stream()
                .map(EventMapper::toEventDtoShort)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDtoFull> findAllByParams(
            Long[] usersIds, String[] states, Long[] categoriesIds,
            String rangeStart, String rangeEnd, Pageable pageable) {
        List<User> userList = new ArrayList<>();
        List<Category> categoryList = new ArrayList<>();

        LocalDateTime start = null;
        LocalDateTime end = null;

        if (rangeStart == null && rangeEnd == null) {
            start = LocalDateTime.now();
        }
        if (rangeStart != null) {
            start = LocalDateTime.parse(URLDecoder.decode(rangeStart, StandardCharsets.UTF_8), formatter);
        }
        if (rangeEnd != null) {
            end = LocalDateTime.parse(URLDecoder.decode(rangeEnd, StandardCharsets.UTF_8), formatter);
        }
        if (start != null && end != null) {
            if (end.isBefore(start)) {
                throw new BadRequestException("Дата начала не может быть позже даты окончания события.");
            }
        }
        if (usersIds != null) {
            userList = userRepository.findByIdIn(Arrays.asList(usersIds));
        }
        if (categoriesIds != null) {
            categoryList = categoryRepository.findByIdIn(Arrays.asList(categoriesIds));
        }

        List<Event> eventList = eventRepository.findAll(getAdminQuery(userList, states, categoryList, start, end),
                pageable).toList();

        return eventList.stream()
                .map(EventMapper::toEventDtoFull)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDtoShort> findAllByParams(
            String text, Long[] categoriesIds, Boolean paid, String rangeStart,
            String rangeEnd, Boolean onlyAvailable, Pageable pageable) {
        LocalDateTime start = null;
        LocalDateTime end = null;

        log.info("Service: {} {} {} {} {} {}", text, categoriesIds, paid, rangeStart, rangeEnd, onlyAvailable);

        if (rangeStart == null && rangeEnd == null) {
            start = LocalDateTime.now();
        }
        if (rangeStart != null) {
            start = LocalDateTime.parse(rangeStart, formatter);
        }
        if (rangeEnd != null) {
            end = LocalDateTime.parse(rangeEnd, formatter);
        }
        if (start != null && end != null) {
            if (end.isBefore(start)) {
                throw new BadRequestException("Дата начала не может быть позже даты окончания события.");
            }
        }

        List<Category> categoryList = null;
        if (categoriesIds != null) {
            categoryList = categoryRepository.findByIdIn(Arrays.asList(categoriesIds));
        }
        List<Event> eventList = eventRepository
                .findAll(getPublicQuery(text, categoryList, paid, start, end, onlyAvailable), pageable).toList();

        return eventList.stream()
                .map(EventMapper::toEventDtoShort)
                .collect(Collectors.toList());
    }

    @Override
    public EventDtoFull findByUser(Long userId, Long eventId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class,
                        String.format(" with id=%d ", userId)));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(Event.class,
                        String.format(" with id=%d ", eventId)));
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ValidationException(Event.class, "Полная информация доступна только создателю мероприятия.");
        }

        return EventMapper.toEventDtoFull(event);
    }

    @Override
    public EventDtoFull findById(Long eventId) {
        Event stored = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(Event.class,
                        String.format(" with id=%d ", eventId)));
        if (!stored.getState().equals(EventState.PUBLISHED)) {
            throw new NotFoundException(Event.class, "Событие не опубликовано.");
        }
        stored.setViews(stored.getViews() + 1);

        return EventMapper.toEventDtoFull(stored);
    }

    private Specification<Event> getAdminQuery(List<User> userList, String[] states, List<Category> categoryList,
                                               LocalDateTime start, LocalDateTime end) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!userList.isEmpty()) {
                predicates.add(criteriaBuilder.in(root.get("initiator")).value(userList));
            }
            if (states != null) {
                List<EventState> stateList = Arrays.stream(states)
                        .map(EventState::valueOf)
                        .collect(Collectors.toList());

                predicates.add(criteriaBuilder.in(root.get("state")).value(stateList));
            }
            if (!categoryList.isEmpty()) {
                predicates.add(criteriaBuilder.in(root.get("category")).value(categoryList));
            }
            if (start != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"),start));
            }
            if (end != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), end));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Specification<Event> getPublicQuery(String text, List<Category> categoryList, Boolean paid,
                                                LocalDateTime start, LocalDateTime end, Boolean onlyAvailable) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (text != null) {
                Predicate textPredicate = criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("annotation")), text.toLowerCase()),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), text.toLowerCase()));
                        predicates.add(textPredicate);
            }
            if (!categoryList.isEmpty()) {
                predicates.add(criteriaBuilder.in(root.get("category")).value(categoryList));
            }
            if (paid != null) {
                predicates.add(criteriaBuilder.equal(root.get("paid"), paid));
            }
            if (start != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"), start));
            }
            if (end != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"), end));
            }
            if (onlyAvailable) {
                predicates.add(criteriaBuilder.lessThan(root.get("confirmedRequests"),
                        root.get("participantLimit")));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
