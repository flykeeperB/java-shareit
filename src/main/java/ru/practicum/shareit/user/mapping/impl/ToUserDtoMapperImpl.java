package ru.practicum.shareit.user.mapping.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapping.ToUserDtoMapper;
import ru.practicum.shareit.user.model.User;

@Component
public class ToUserDtoMapperImpl implements ToUserDtoMapper {

    @Override
    public UserDto map(User user) {
        UserDto result = new UserDto();

        result.setId(user.getId());
        result.setName(user.getName());
        result.setEmail(user.getEmail());

        return result;
    }

}
