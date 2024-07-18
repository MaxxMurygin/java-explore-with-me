package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.CompilationMapper;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.dto.compilation.UpdateCompilationRequest;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.repository.CompilationRepository;
import ru.practicum.ewm.repository.EventRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DefaultCompilationService implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CompilationDto create(NewCompilationDto newCompilationDto) {

        List<Event> eventList = new ArrayList<>();

        if (newCompilationDto.getPinned() == null) {
            newCompilationDto.setPinned(false);
        }
        if (newCompilationDto.getEvents() != null) {
            eventList = eventRepository.findAllById(newCompilationDto.getEvents());
        }

        Compilation newCompilation = Compilation.builder()
                .title(newCompilationDto.getTitle())
                .pinned(newCompilationDto.getPinned())
                .events(new HashSet<>(eventList))
                .build();

        return CompilationMapper.toCompilationDto(
                compilationRepository.save(newCompilation));
    }

    @Override
    @Transactional
    public void remove(Long compilationId) {

        compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException(Category.class,
                        String.format(" with id=%d ", compilationId)));

        compilationRepository.deleteById(compilationId);
    }

    @Override
    @Transactional
    public CompilationDto update(
            Long compilationId, UpdateCompilationRequest changedCompilationDto) {

        Compilation stored = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException(Category.class,
                        String.format(" with id=%d ", compilationId)));
        List<Event> eventList = new ArrayList<>();

        if (changedCompilationDto.getEvents() != null) {
            eventList = eventRepository.findAllById(changedCompilationDto
                    .getEvents()
                    .stream()
                    .distinct()
                    .collect(Collectors.toList()));
        }
        stored.setEvents(new HashSet<>(eventList));

        if (changedCompilationDto.getPinned() != null) {
            stored.setPinned(changedCompilationDto.getPinned());
        }
        if (changedCompilationDto.getTitle() != null) {
            stored.setTitle(changedCompilationDto.getTitle());
        }

        stored = compilationRepository.save(stored);

        return CompilationMapper.toCompilationDto(stored);
    }

    @Override
    public List<CompilationDto> findAll(Boolean pinned, Pageable pageable) {
        List<Compilation> compilations;
        if (!pinned) {
            compilations = compilationRepository.findAll(pageable).toList();
        } else {
            compilations = compilationRepository.findAllByPinned(true, pageable);
        }

        return compilations.stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto findById(Long compilationId) {

        compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException(Category.class,
                        String.format(" with id=%d ", compilationId)));

        return CompilationMapper
                .toCompilationDto(compilationRepository
                        .findById(compilationId).orElseThrow(() -> new NotFoundException(Category.class,
                                String.format(" with id=%d ", compilationId))));
    }
}