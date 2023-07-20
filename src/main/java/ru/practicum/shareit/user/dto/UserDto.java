package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.AbstractDto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Builder
@Data
@AllArgsConstructor
public class UserDto extends AbstractDto {
    private String name;

    @NotBlank(message = "Не задан адрес электронной почты")
    @Email(message = "Неверный адрес электронной почты")
    private String email;
}
