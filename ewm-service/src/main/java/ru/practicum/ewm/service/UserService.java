package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(UserDto userDto);

    void remove(Long userId);

    UserDto update(Long userId, UserDto userDto);

    List<UserDto> findAll();

    UserDto findById(Long userId);
}
