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
import ru.practicum.ewm.dto.user.NewUserRequest;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.service.CategoryService;
import ru.practicum.ewm.service.UserService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/admin")
@Slf4j
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final CategoryService categoryService;
    @GetMapping("/users")
    public List<UserDto> findUsers(@RequestParam(name = "ids", required = false) Long[] ids,
                              @RequestParam(name = "from", defaultValue = "0") Integer from,
                              @RequestParam(name = "size", defaultValue = "10") Integer size) {

        if (ids != null) {
            log.info("Запрос на вывод пользователей с ids = {}", Arrays.toString(ids));
            return userService.findByIds(ids);
        }

        Pageable userPage = PageRequest.of(from, size, Sort.by("id").ascending());
        return userService.findAll(userPage).getContent();
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody NewUserRequest newUserRequest) {

        return userService.create(newUserRequest);
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUser(@PathVariable Long id) {
        log.info("Запрос на удаление пользователя с id = {}", id);

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
    public CategoryDto updateCategory(@PathVariable Long id,
                                      @Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Запрос на изменение категории с id = {}", id);

        return categoryService.update(id, newCategoryDto);
    }
}
