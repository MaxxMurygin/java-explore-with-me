package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.dto.compilation.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    CompilationDto create(NewCompilationDto newCompilationDto);

    void remove(Long compilationId);

    CompilationDto update(Long compilationId, UpdateCompilationRequest newCompilationDto);

    List<CompilationDto> findAll(Boolean pinned, Pageable pageable);

    CompilationDto findById(Long compilationId);
}
