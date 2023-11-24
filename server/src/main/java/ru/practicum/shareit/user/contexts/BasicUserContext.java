package ru.practicum.shareit.user.contexts;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.user.dto.UserDto;

@Data
@SuperBuilder
public class BasicUserContext {
    private final UserDto userDto;
}
