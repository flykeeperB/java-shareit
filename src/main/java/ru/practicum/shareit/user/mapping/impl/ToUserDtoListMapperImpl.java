package ru.practicum.shareit.user.mapping.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapping.ToUserDtoListMapper;
import ru.practicum.shareit.user.mapping.ToUserDtoMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ToUserDtoListMapperImpl implements ToUserDtoListMapper {

    private final ToUserDtoMapper toUserDtoMapper;

    @Override
    public List<UserDto> map(List<User> source) {
        return source
                .stream()
                .map(toUserDtoMapper::map)
                .collect(Collectors.toList());
    }
}
