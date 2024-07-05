package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.CompilationMapper;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.dto.event.EventDtoShort;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.CompilationEvent;
import ru.practicum.ewm.repository.CompilationEventRepository;
import ru.practicum.ewm.repository.CompilationRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultCompilationService implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationEventRepository compilationEventRepository;
    private final EventService eventService;

    @Override
    @Transactional
    public CompilationDto create(NewCompilationDto newCompilationDto) {
        if (newCompilationDto.getPinned() == null) {
            newCompilationDto.setPinned(false);
        }
        Compilation newCompilation = compilationRepository
                .save(CompilationMapper.fromNewCompilationDto(newCompilationDto));
        List<Long> eventsIds = newCompilationDto.getEvents();
        List<EventDtoShort> events = new ArrayList<>();
        if (eventsIds != null) {
            eventsIds = eventsIds.stream().distinct().collect(Collectors.toList());
            events = eventService.findAllByIds(eventsIds);
            Long newCompilationId = newCompilation.getId();
            List<CompilationEvent> compilationEventList = eventsIds.stream()
                    .map(e -> CompilationEvent.builder()
                            .compilationId(newCompilationId)
                            .eventId(e)
                            .build())
                    .collect(Collectors.toList());
            compilationEventRepository.saveAll(compilationEventList);
        }

        return CompilationMapper.toCompilationDto(newCompilation, events);
    }

    @Override
    @Transactional
    public void remove(Long compilationId) {
        compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException(Category.class,
                        String.format(" with id=%d ", compilationId)));

        List<Long> eventsIds = compilationEventRepository
                .findAllByCompilationId(compilationId)
                .stream()
                .map(CompilationEvent::getEventId)
                .collect(Collectors.toList());

        if (!eventsIds.isEmpty()) {
            compilationEventRepository.deleteAllByCompilationId(compilationId);
        }
        compilationRepository.deleteById(compilationId);
    }

    @Override
    @Transactional
    public CompilationDto update(Long compilationId, NewCompilationDto changedCompilationDto) {
        Compilation stored = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException(Category.class,
                        String.format(" with id=%d ", compilationId)));
        List<Long> changedEventsIds = changedCompilationDto.getEvents();
        List<Long> storedEventsIds = compilationEventRepository
                .findAllByCompilationId(compilationId)
                .stream()
                .map(CompilationEvent::getEventId)
                .collect(Collectors.toList());

        changedEventsIds = changedEventsIds.stream().distinct().collect(Collectors.toList());
        stored.setPinned(changedCompilationDto.getPinned());
        stored.setTitle(changedCompilationDto.getTitle());

        if (!changedEventsIds.equals(storedEventsIds)) {
            compilationEventRepository.deleteAllByCompilationId(compilationId);
            List<CompilationEvent> compilationEventList = changedEventsIds.stream()
                    .map(e -> CompilationEvent.builder()
                            .compilationId(compilationId)
                            .eventId(e)
                            .build())
                    .collect(Collectors.toList());
            compilationEventRepository.saveAll(compilationEventList);
        }
        List<EventDtoShort> events = eventService.findAllByIds(changedEventsIds);
        Compilation changedCompilation = compilationRepository.save(stored);

        return CompilationMapper.toCompilationDto(changedCompilation, events);
    }

    @Override
    public List<CompilationDto> findAll(Boolean pinned, Pageable pageable) {
        List<Compilation> compilations;
        if (!pinned) {
            compilations = compilationRepository.findAll(pageable).toList();
        } else {
            compilations = compilationRepository.findAllByPinned(true, pageable);
        }

        List<Long> compilationsIds = compilations.stream()
                .map(Compilation::getId)
                .collect(Collectors.toList());
        List<CompilationEvent> compilationEvents = compilationEventRepository.findAllByCompilationIdIn(compilationsIds);
        List<Long> eventsIds = compilationEvents.stream()
                .map(CompilationEvent::getEventId)
                .distinct()
                .collect(Collectors.toList());
        List<EventDtoShort> events = eventService.findAllByIds(eventsIds);
        Map<Long, EventDtoShort> eventsMap = events.stream()
                .collect(Collectors.toMap(EventDtoShort::getId, event -> event));
        Map<Long, List<EventDtoShort>> megaMap = new HashMap<>();

        for (CompilationEvent ce:compilationEvents) {
            Long compId = ce.getCompilationId();
            Long eventId = ce.getEventId();
            List<EventDtoShort> eventList;

            if (megaMap.get(compId) == null) {
                eventList = new ArrayList<>();
                eventList.add(eventsMap.get(eventId));
                megaMap.put(compId, eventList);
            } else {
                eventList = megaMap.get(compId);
                eventList.add(eventsMap.get(eventId));
                megaMap.put(compId, eventList);
            }
        };
        return compilations.stream()
                .map(c -> CompilationMapper.toCompilationDto(c, megaMap.get(c.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto findById(Long compilationId) {
        Compilation stored = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException(Category.class,
                        String.format(" with id=%d ", compilationId)));

        List<Long> eventsIds = compilationEventRepository
                .findAllByCompilationId(compilationId)
                .stream()
                .map(CompilationEvent::getEventId)
                .collect(Collectors.toList());
        List<EventDtoShort> events = eventService.findAllByIds(eventsIds);
        return CompilationMapper.toCompilationDto(stored, events);
    }
}
