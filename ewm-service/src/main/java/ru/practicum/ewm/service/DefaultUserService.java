package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional(readOnly = true)
public class DefaultUserService implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto create(NewUserRequest newUserRequest) {

//        String email = newUserRequest.getEmail();
//        String name = newUserRequest.getName();

//        if (userRepository.findByEmail(email).isPresent()) {
//            throw new AlreadyExistException(User.class,
//                    String.format(" with email = %s ", email));
//        }
//
//        if (userRepository.findByName(name).isPresent()) {
//            throw new AlreadyExistException(User.class,
//                    String.format(" with name = %s ", name));
//        }

        return UserMapper.toDto(
                userRepository.save(UserMapper.fromNewUserRequest(newUserRequest)));
    }

    @Override
    @Transactional
    public void remove(Long userId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(User.class, String.format(" with id=%d ", userId)));

        userRepository.deleteById(userId);
    }

    @Override
    public List<UserDto> findAll(Pageable pageable) {

        return userRepository.findAll(pageable)
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> findByIds(Long[] ids) {
        return userRepository.findByIdIn(Arrays.asList(ids))
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }
}