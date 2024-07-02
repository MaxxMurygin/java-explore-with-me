package ru.practicum.ewm.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.dto.user.NewUserRequest;
import ru.practicum.ewm.dto.user.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(NewUserRequest newUserRequest);

    void remove(Long userId);

    Page<UserDto> findAll(Pageable pageable);

    List<UserDto> findByIds(Long[] ids);
}
