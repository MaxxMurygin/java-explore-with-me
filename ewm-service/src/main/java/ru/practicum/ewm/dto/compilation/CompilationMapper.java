package ru.practicum.ewm.dto.compilation;

import ru.practicum.ewm.dto.event.EventMapper;
import ru.practicum.ewm.model.Compilation;

import java.util.stream.Collectors;

public class CompilationMapper {
    public static Compilation fromNewCompilationDto(NewCompilationDto newCompilationDto) {
        return Compilation.builder()
                .title(newCompilationDto.getTitle())
                .pinned(newCompilationDto.getPinned())
                .build();
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .events(compilation.getEvents()
                        .stream()
                        .map(EventMapper::toEventDtoShort)
                        .collect(Collectors.toList()))
                .build();
    }
}
