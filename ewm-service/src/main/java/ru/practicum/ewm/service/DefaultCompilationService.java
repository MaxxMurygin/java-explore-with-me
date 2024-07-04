package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;

import java.awt.print.Pageable;
import java.util.List;

public class DefaultCompilationService implements CompilationService {
    @Override
    public CompilationDto create(NewCompilationDto newCompilationDto) {
        return null;
    }

    @Override
    public void remove(Long compilationId) {

    }

    @Override
    public CompilationDto update(Long compilationId, NewCompilationDto newCompilationDto) {
        return null;
    }

    @Override
    public List<CompilationDto> findAll(Boolean pinned, Pageable pageable) {
        return null;
    }

    @Override
    public CompilationDto findById(Long compilationId) {
        return null;
    }
}
