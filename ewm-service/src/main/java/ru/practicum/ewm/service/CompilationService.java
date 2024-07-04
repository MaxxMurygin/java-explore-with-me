package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;

import java.awt.print.Pageable;
import java.util.List;

public interface CompilationService {
    CompilationDto create(NewCompilationDto newCompilationDto);

    void remove(Long compilationId);

    CompilationDto update(Long compilationId, NewCompilationDto newCompilationDto);

    List<CompilationDto> findAll(Boolean pinned, Pageable pageable);

    CompilationDto findById(Long compilationId);
}
