package ru.practicum.shareit.user.mapping.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapping.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto mapToUserDto(User user) {
        UserDto result = new UserDto();

        result.setId(user.getId());
        result.setName(user.getName());
        result.setEmail(user.getEmail());

        return result;
    }

    @Override
    public User mapToUser(UserDto userDto) {
        User result = new User();

        result.setId(userDto.getId());
        result.setName(userDto.getName());
        result.setEmail(userDto.getEmail());

        return result;
    }

    @Override
    public List<UserDto> mapToUserDto(List<User> source) {
        return source
                .stream()
                .map(this::mapToUserDto)
                .collect(Collectors.toList());
    }
}
