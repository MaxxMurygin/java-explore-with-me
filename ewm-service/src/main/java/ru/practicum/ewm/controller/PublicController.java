package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.event.EventDtoFull;
import ru.practicum.ewm.dto.event.EventDtoShort;
import ru.practicum.ewm.service.CategoryService;
import ru.practicum.ewm.service.EventService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping
@Slf4j
@RequiredArgsConstructor
public class PublicController {
    private final EventService eventService;
    private final CategoryService categoryService;

    @GetMapping("/events")
    public List<EventDtoShort> getAllEvents(@RequestParam(name = "text", required = false) String text,
                                    @RequestParam(name = "categories", required = false) Long[] categoriesIds,
                                    @RequestParam(name = "paid", required = false) Boolean paid,
                                    @RequestParam(name = "rangeStart", required = false) String start,
                                    @RequestParam(name = "rangeEnd", required = false) String end,
                                    @RequestParam(name = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
                                    @RequestParam(name = "sort", required = false) String sort,
                                    @RequestParam(name = "from", defaultValue = "0") Integer from,
                                    @RequestParam(name = "size", defaultValue = "10") Integer size) {

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

        return eventService.findAllByParams(text, categoriesIds, paid, start, end, onlyAvailable, userPage);
    }

    @GetMapping("/events/{eventId}")
    public EventDtoFull getEvent(@PathVariable(name = "eventId") @Positive Long eventId) {

        return eventService.findById(eventId);
    }

    private enum SortBy {
        EVENT_DATE, VIEWS
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
}
