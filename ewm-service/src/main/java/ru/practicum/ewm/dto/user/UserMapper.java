package ru.practicum.ewm.dto.user;

import ru.practicum.ewm.model.User;

public class UserMapper {
    public static UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static UserDtoShort toDtoShort(User user) {
        return UserDtoShort.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public static User fromNewUserRequest(NewUserRequest newUserRequest) {
        User user = new User();

        user.setName(newUserRequest.getName());
        user.setEmail(newUserRequest.getEmail());
        return user;
    }
}
