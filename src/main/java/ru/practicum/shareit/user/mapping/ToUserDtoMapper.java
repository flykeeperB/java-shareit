package ru.practicum.shareit.user.mapping;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public interface ToUserDtoMapper {
    UserDto map(User user);
}
