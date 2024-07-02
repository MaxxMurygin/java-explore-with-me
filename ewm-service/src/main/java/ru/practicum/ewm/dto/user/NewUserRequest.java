package ru.practicum.ewm.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Getter
public class NewUserRequest {
    @NotBlank(message = "[name] cannot be blank")
    @Size(min = 2, max = 50)
    private String name;
    @NotBlank(message = "[email] cannot be blank")
    @Email(message = "wrong email")
    private String email;
}
