package ru.practicum.shareit.user.dto;

import lombok.*;
import ru.practicum.shareit.AbstractDto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto extends AbstractDto {

    private String name;

    @NotBlank(message = "Не задан адрес электронной почты")
    @Email(message = "Неверный адрес электронной почты")
    private String email;
}
