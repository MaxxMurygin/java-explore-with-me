package ru.practicum.ewm.dto.compilation;

import ru.practicum.ewm.dto.event.EventDtoShort;
import ru.practicum.ewm.model.Compilation;

import java.util.List;

public class CompilationMapper {
    public static Compilation fromNewCompilationDto(NewCompilationDto newCompilationDto) {
        return Compilation.builder()
                .title(newCompilationDto.getTitle())
                .pinned(newCompilationDto.getPinned())
                .build();
    }

    public static CompilationDto toCompilationDto(Compilation compilation, List<EventDtoShort> events) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .events(events)
                .build();
    }
}
