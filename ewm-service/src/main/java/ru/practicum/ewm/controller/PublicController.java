package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.client.StatsClient;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.event.EventDtoFull;
import ru.practicum.ewm.dto.event.EventDtoShort;
import ru.practicum.ewm.service.CategoryService;
import ru.practicum.ewm.service.CompilationService;
import ru.practicum.ewm.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping
@Slf4j
@RequiredArgsConstructor
public class PublicController {
    private final EventService eventService;
    private final CategoryService categoryService;
    private final CompilationService compilationService;
    private final StatsClient statsClient;

    @GetMapping("/events")
    public List<EventDtoShort> getAllEvents(@RequestParam(name = "text", required = false) String text,
                                    @RequestParam(name = "categories", required = false) Long[] categoriesIds,
                                    @RequestParam(name = "paid", required = false) Boolean paid,
                                    @RequestParam(name = "rangeStart", required = false) String start,
                                    @RequestParam(name = "rangeEnd", required = false) String end,
                                    @RequestParam(name = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
                                    @RequestParam(name = "sort", required = false) String sort,
                                    @RequestParam(name = "from", defaultValue = "0") Integer from,
                                    @RequestParam(name = "size", defaultValue = "10") Integer size,
                                    HttpServletRequest request) {

        Sort order;
        if (sort != null) {
            SortBy sortBy = SortBy.valueOf(sort);

            if (sortBy.equals(SortBy.VIEWS)) {
                order = Sort.by("views").descending();
            } else {
                order = Sort.by("eventDate").ascending();
            }
        } else {
            order = Sort.by("id").ascending();
        }
        Pageable userPage = PageRequest.of(from / size, size, order);
        List<EventDtoShort> events = eventService.findAllByParams(text, categoriesIds, paid,
                                                                start, end, onlyAvailable, userPage);
        statsClient.post("ewm-service", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());
        return events;
    }

    @GetMapping("/events/{eventId}")
    public EventDtoFull getEvent(@PathVariable(name = "eventId") @Positive Long eventId,
                                 HttpServletRequest request) {
        EventDtoFull event = eventService.findById(eventId);

        statsClient.post("ewm-service", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());
        return event;
    }

    @GetMapping("/categories")
    public List<CategoryDto> findCategories(@RequestParam(name = "from", defaultValue = "0") Integer from,
                                            @RequestParam(name = "size", defaultValue = "10") Integer size) {

        Pageable userPage = PageRequest.of(from, size, Sort.by("id").ascending());
        return categoryService.findAll(userPage);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto findCategories(@PathVariable(name = "catId") Long catId) {
        return categoryService.findById(catId);
    }

    @GetMapping("/compilations")
    public List<CompilationDto> findCompilations(@RequestParam(name = "pinned", defaultValue = "false") Boolean pinned,
                                                 @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                 @RequestParam(name = "size", defaultValue = "10") Integer size) {

        Pageable userPage = PageRequest.of(from, size, Sort.by("id").ascending());
        return compilationService.findAll(pinned, userPage);
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto findCompilations(@PathVariable(name = "compId") Long compId) {
        return compilationService.findById(compId);
    }

    private enum SortBy {
        EVENT_DATE, VIEWS
    }
}
