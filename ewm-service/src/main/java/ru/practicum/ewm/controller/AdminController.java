package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.category.NewCategoryDto;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.dto.compilation.UpdateCompilationRequest;
import ru.practicum.ewm.dto.event.EventDtoFull;
import ru.practicum.ewm.dto.event.UpdateEventAdminRequest;
import ru.practicum.ewm.dto.user.NewUserRequest;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.service.CategoryService;
import ru.practicum.ewm.service.CompilationService;
import ru.practicum.ewm.service.EventService;
import ru.practicum.ewm.service.UserService;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/admin")
@Slf4j
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final CategoryService categoryService;
    private final EventService eventService;
    private final CompilationService compilationService;

    @GetMapping("/users")
    public List<UserDto> findUsers(
            @RequestParam(name = "ids", required = false) Long[] ids,
            @RequestParam(name = "from", defaultValue = "0") Integer from,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {

        if (ids != null) {
            return userService.findByIds(ids);
        }

        Pageable userPage = PageRequest.of(from, size, Sort.by("id").ascending());
        return userService.findAll(userPage);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody NewUserRequest newUserRequest) {

        return userService.create(newUserRequest);
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUser(@PathVariable Long id) {
        userService.remove(id);
    }

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {

        return categoryService.create(newCategoryDto);
    }

    @DeleteMapping("/categories/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCategory(@PathVariable Long id) {
        log.info("Запрос на удаление категории с id = {}", id);

        categoryService.remove(id);
    }

    @PatchMapping("/categories/{id}")
    public CategoryDto updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Запрос на изменение категории с id = {}", id);

        return categoryService.update(id, newCategoryDto);
    }

    @GetMapping("/events")
    public List<EventDtoFull> getAllEvents(@RequestParam(name = "users", required = false) Long[] usersIds,
                                           @RequestParam(name = "states", required = false) String[] states,
                                           @RequestParam(name = "categories", required = false) Long[] categoriesIds,
                                           @RequestParam(name = "rangeStart", required = false) String start,
                                           @RequestParam(name = "rangeEnd", required = false) String end,
                                           @RequestParam(name = "from", defaultValue = "0") Integer from,
                                           @RequestParam(name = "size", defaultValue = "10") Integer size) {

        Pageable userPage = PageRequest.of(from / size, size, Sort.by("eventDate").ascending());

        log.info("Admin get all events: {} {} {} {} {}", usersIds, states, categoriesIds, start, end);
        return eventService.findAllByParams(usersIds, states, categoriesIds, start, end, userPage);
    }

    @PatchMapping("/events/{eventId}")
    public EventDtoFull patchEvent(
            @PathVariable(name = "eventId") Long eventId,
            @Valid @RequestBody UpdateEventAdminRequest changedEventDto) {

        return eventService.update(eventId, changedEventDto);
    }

    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {

        return compilationService.create(newCompilationDto);
    }

    @PatchMapping("/compilations/{compId}")
    public CompilationDto updateCompilation(
            @PathVariable(name = "compId") Long compId,
            @Valid @RequestBody UpdateCompilationRequest updatedCompilationDto) {

        return compilationService.update(compId, updatedCompilationDto);
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCompilation(@PathVariable(name = "compId") Long compId) {

        compilationService.remove(compId);
    }
}
