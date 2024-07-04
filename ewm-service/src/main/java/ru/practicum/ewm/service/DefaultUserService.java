package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.user.NewUserRequest;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.dto.user.UserMapper;
import ru.practicum.ewm.exception.AlreadyExistException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService{
    private final UserRepository userRepository;
    @Override
    public UserDto create(NewUserRequest newUserRequest) {
        String email = newUserRequest.getEmail();
        String name = newUserRequest.getName();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new AlreadyExistException(User.class,
                    String.format(" with email = %s ", email));
        }

        if (userRepository.findByName(name).isPresent()) {
            throw new AlreadyExistException(User.class,
                    String.format(" with name = %s ", name));
        }

        return UserMapper.toDto(userRepository.save(UserMapper.fromNewUserRequest(newUserRequest)));
    }

    @Override
    public void remove(Long userId) {
        userRepository.findById(userId).
                orElseThrow(() -> new NotFoundException(User.class, String.format(" with id=%d ", userId)));

        userRepository.deleteById(userId);

    }

    @Override
    public Page<UserDto> findAll(Pageable pageable) {

        return new PageImpl<>(userRepository.findAll(pageable).stream()
                .map(UserMapper::toDto).
                collect(Collectors.toList()));
    }

    @Override
    public List<UserDto> findByIds(Long[] ids) {
        return userRepository.findByIdIn(Arrays.asList(ids)).stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }
}
