package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface ExternalUserService {

    User retrieve(Long id);

    List<UserDto> retrieve(List<Long> ids, Optional<Long> userId);

}
