package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.common.EwmDateFormatter;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.category.CategoryMapper;
import ru.practicum.ewm.dto.event.*;
import ru.practicum.ewm.dto.user.UserDtoShort;
import ru.practicum.ewm.dto.user.UserMapper;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model.enums.EventState;
import ru.practicum.ewm.model.enums.EventStateAdminAction;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.UserRepository;

import javax.persistence.criteria.Predicate;
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
public class DefaultEventService implements EventService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final DateTimeFormatter formatter = EwmDateFormatter.getFormatter();

    @Override
    public EventDtoFull create(Long initiatorId, NewEventDto newEventDto) {
        Long categoryId = newEventDto.getCategory();
        User initiator = userRepository.findById(initiatorId)
                .orElseThrow(() -> new NotFoundException(User.class,
                                                            String.format(" with id=%d ", initiatorId)));
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(Category.class,
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

        if (newEvent.getEventDate().isBefore(newEvent.getCreatedOn().plusHours(2))) {
            throw new ValidationException(Event.class,
                    "Событие должно начаться не ранее, чем через 2 часа после создания");
        }

        return EventMapper.toEventDtoFull(eventRepository.save(newEvent),
                CategoryMapper.toDto(category),
                UserMapper.toDtoShort(initiator));
    }

    @Override
    public EventDtoFull update(Long initiatorId, Long eventId, UpdateEventUserRequest changedEventDto) {
        User initiator = userRepository.findById(initiatorId)
                .orElseThrow(() -> new NotFoundException(User.class,
                        String.format(" with id=%d ", initiatorId)));
        Event stored = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(Event.class,
                        String.format(" with id=%d ", eventId)));
        Long categoryId = stored.getCategoryId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(Category.class,
                        String.format(" with id=%d ", categoryId)));

        if (stored.getState().equals(EventState.PUBLISHED)) {
            throw new ValidationException(Event.class, "Нельзя изменить опубликованные события");
        }
        if (changedEventDto.getAnnotation() != null) {
            stored.setAnnotation(changedEventDto.getAnnotation());
        }
        if (changedEventDto.getCategory() != null) {
            Long changedCategoryId = changedEventDto.getCategory();
            category = categoryRepository.findById(changedCategoryId)
                    .orElseThrow(() -> new NotFoundException(Category.class,
                            String.format(" with id=%d ", changedCategoryId)));

            stored.setCategoryId(changedCategoryId);
        }
        if (changedEventDto.getDescription() != null) {
            stored.setDescription(changedEventDto.getDescription());
        }
        if (changedEventDto.getEventDate() != null) {
            stored.setEventDate(LocalDateTime.parse(changedEventDto.getEventDate(), formatter));
        }
        if (stored.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException(Event.class,
                    "Нельзя редактировать событие, менее чем за 2 часа до его начала");
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

        return EventMapper.toEventDtoFull(eventRepository.save(stored),
                CategoryMapper.toDto(category),
                UserMapper.toDtoShort(initiator));
    }

    @Override
    public EventDtoFull update(Long eventId, UpdateEventAdminRequest changedEventDto) {
        Event stored = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(Event.class,
                        String.format(" with id=%d ", eventId)));

        if (stored.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ValidationException(Event.class,
                    "Нельзя изменять событие, менее чем за 1 час до его начала");
        }
        stored.setPublishedOn(LocalDateTime.now());
        if (changedEventDto.getStateAction().equals(EventStateAdminAction.PUBLISH_EVENT)) {
            if (!stored.getState().equals(EventState.PENDING)) {
                throw new ValidationException(Event.class,
                        "Событие можно публиковать, только если оно в состоянии ожидания публикации.");
            }
            stored.setState(EventState.PUBLISHED);
        } else {
            if (stored.getState().equals(EventState.PENDING)) {
                throw new ValidationException(Event.class,
                        "Событие можно отклонить, только если оно еще не опубликовано.");
            }
            stored.setState(EventState.CANCELED);
        }

        stored.setDescription(changedEventDto.getDescription());
        stored.setEventDate(LocalDateTime.parse(changedEventDto.getEventDate(), formatter));
        stored.setLocationLat(changedEventDto.getLocation().getLat());
        stored.setLocationLon(changedEventDto.getLocation().getLon());
        stored.setPaid(changedEventDto.getPaid());
        stored.setParticipantLimit(changedEventDto.getParticipantLimit());
        stored.setRequestModeration(changedEventDto.getRequestModeration());
        stored.setTitle(changedEventDto.getTitle());

        return null;
    }

    @Override
    public List<EventDtoFull> findAllByUser(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class,
                        String.format(" with id=%d ", userId)));
        List<Event> eventList = eventRepository.findAllByInitiatorId(userId,pageable);
        List<Long> categoryIds = eventList.stream()
                .map(Event::getCategoryId)
                .collect(Collectors.toList());
        Map<Long, CategoryDto> categoryMap = categoryRepository.findByIdIn(categoryIds).stream()
                .map(CategoryMapper::toDto)
                .collect(Collectors.toMap(CategoryDto::getId, c -> c));

        return eventList.stream()
                .map(e -> EventMapper.toEventDtoFull(e,
                        categoryMap.get(e.getCategoryId()),
                        UserMapper.toDtoShort(user)))
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDtoFull> findAllByParams(Long[] usersIds, String[] states, Long[] categoriesIds,
                                              String start, String end, Pageable pageable) {
        List<Event> eventList = eventRepository.findAll(getAdminQuery(usersIds, states, categoriesIds, start, end),
                pageable).toList();
        List<Long> categoryIds = eventList.stream()
                .map(Event::getCategoryId)
                .collect(Collectors.toList());
        Map<Long, CategoryDto> categoryMap = categoryRepository.findByIdIn(categoryIds).stream()
                .map(CategoryMapper::toDto)
                .collect(Collectors.toMap(CategoryDto::getId, c -> c));
        List<Long> initiatorsIds = eventList.stream()
                .map(Event::getInitiatorId)
                .collect(Collectors.toList());
        Map<Long, UserDtoShort> ininiatorsMap = userRepository.findByIdIn(initiatorsIds).stream()
                .map(UserMapper::toDtoShort)
                .collect(Collectors.toMap(UserDtoShort::getId, u -> u));


        return eventList.stream()
                .map(e -> EventMapper.toEventDtoFull(e,
                        categoryMap.get(e.getCategoryId()),
                        ininiatorsMap.get(e.getInitiatorId())))
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDtoShort> findAllByParams(String text, Long[] categoriesIds, Boolean paid,
                                               String start, String end, Boolean onlyAvailable, Pageable pageable) {
        List<Event> eventList = eventRepository
                .findAll(getPublicQuery(text, categoriesIds, paid, start, end, onlyAvailable), pageable).toList();
        List<Long> categoryIds = eventList.stream()
                .map(Event::getCategoryId)
                .collect(Collectors.toList());
        Map<Long, CategoryDto> categoryMap = categoryRepository.findByIdIn(categoryIds).stream()
                .map(CategoryMapper::toDto)
                .collect(Collectors.toMap(CategoryDto::getId, c -> c));
        List<Long> initiatorsIds = eventList.stream()
                .map(Event::getInitiatorId)
                .collect(Collectors.toList());
        Map<Long, UserDtoShort> ininiatorsMap = userRepository.findByIdIn(initiatorsIds).stream()
                .map(UserMapper::toDtoShort)
                .collect(Collectors.toMap(UserDtoShort::getId, u -> u));

        return eventList.stream()
                .map(e -> EventMapper.toEventDtoShort(e,
                        categoryMap.get(e.getCategoryId()),
                        ininiatorsMap.get(e.getInitiatorId())))
                .collect(Collectors.toList());
    }

    @Override
    public EventDtoFull findByUser(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class,
                        String.format(" with id=%d ", userId)));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(Event.class,
                        String.format(" with id=%d ", eventId)));
        if (!event.getInitiatorId().equals(userId)) {
            throw new ValidationException(Event.class, "Полная информация доступна только создателю мероприятия.");
        }

        Category category = categoryRepository.findById(event.getCategoryId())
                .orElseThrow(() -> new NotFoundException(Category.class,
                        String.format(" with id=%d ", event.getCategoryId())));
        return EventMapper.toEventDtoFull(event,
                CategoryMapper.toDto(category),
                UserMapper.toDtoShort(user));
    }

    @Override
    public EventDtoFull findById(Long eventId) {
        Event stored = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(Event.class,
                        String.format(" with id=%d ", eventId)));
        if (!stored.getState().equals(EventState.PUBLISHED)) {
            throw new NotFoundException(Event.class, "Событие не опубликовано.");
        }
        Long initiatorId = stored.getInitiatorId();
        User initiator = userRepository.findById(initiatorId)
                .orElseThrow(() -> new NotFoundException(User.class,
                        String.format(" with id=%d ", initiatorId)));
        Long categoryId = stored.getCategoryId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException(Category.class,
                        String.format(" with id=%d ", categoryId)));

        return EventMapper.toEventDtoFull(stored,
                CategoryMapper.toDto(category),
                UserMapper.toDtoShort(initiator));
    }

    private Specification<Event> getAdminQuery(Long[] usersIds, String[] states, Long[] categoriesIds,
                                               String start, String end) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (usersIds != null) {
                predicates.add(criteriaBuilder.in(root.get("initiatorId")).value(Arrays.asList(usersIds)));
            }
            if (states != null) {
                List<EventState> stateList = Arrays.stream(states)
                        .map(EventState::valueOf)
                        .collect(Collectors.toList());

                predicates.add(criteriaBuilder.in(root.get("state")).value(stateList));
            }
            if (categoriesIds != null) {
                predicates.add(criteriaBuilder.in(root.get("categoryId")).value(Arrays.asList(categoriesIds)));
            }
            if (start != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"),
                        LocalDateTime.parse(start, formatter)));
            }
            if (end != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"),
                        LocalDateTime.parse(end, formatter)));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Specification<Event> getPublicQuery(String text,Long[] categoriesIds, Boolean paid,
                                                String start, String end, Boolean onlyAvailable) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (text != null) {
                predicates.add(criteriaBuilder.like(root.get("annotation"), text));
                predicates.add(criteriaBuilder.like(root.get("description"), text));
            }
            if (categoriesIds != null) {
                predicates.add(criteriaBuilder.in(root.get("categoryId")).value(Arrays.asList(categoriesIds)));
            }
            if (paid != null) {
                predicates.add(criteriaBuilder.equal(root.get("paid"), paid));
            }
            if (start != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("eventDate"),
                        LocalDateTime.parse(start, formatter)));
            }
            if (end != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("eventDate"),
                        LocalDateTime.parse(end, formatter)));
            }
            if (start == null && end == null) {
                predicates.add(criteriaBuilder.greaterThan(root.get("eventDate"), LocalDateTime.now()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
