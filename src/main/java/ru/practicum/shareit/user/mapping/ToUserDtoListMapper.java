package ru.practicum.shareit.user.mapping;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ToUserDtoListMapper {
    List<UserDto> map(List<User> source);
}
