package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    protected Long id;

    private String name;

    @NotBlank(message = "Не задан адрес электронной почты")
    @Email(message = "Неверный адрес электронной почты")
    private String email;
}
