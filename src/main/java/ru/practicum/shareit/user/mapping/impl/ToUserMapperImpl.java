package ru.practicum.shareit.user.mapping.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapping.ToUserMapper;
import ru.practicum.shareit.user.model.User;

@Component
public class ToUserMapperImpl implements ToUserMapper {
    @Override
    public User map(UserDto userDto) {
        User result = new User();

        result.setId(userDto.getId());
        result.setName(userDto.getName());
        result.setEmail(userDto.getEmail());

        return result;
    }
}
