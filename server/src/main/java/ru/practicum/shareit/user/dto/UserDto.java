package ru.practicum.shareit.user.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    protected Long id;
    private String name;
    private String email;
}
