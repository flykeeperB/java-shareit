package ru.practicum.shareit.user.mapping;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserMapper {

    UserDto mapToUserDto(User user);

    User mapToUser(UserDto userDto);

    List<UserDto> mapToUserDto(List<User> source);

}
