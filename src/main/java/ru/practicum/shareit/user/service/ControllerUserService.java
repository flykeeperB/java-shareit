package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.contexts.CreateUserContext;
import ru.practicum.shareit.user.contexts.DeleteUserContext;
import ru.practicum.shareit.user.contexts.RetrieveUserContext;
import ru.practicum.shareit.user.contexts.UpdateUserContext;

import java.util.List;

public interface ControllerUserService {

    UserDto create(CreateUserContext request);

    UserDto retrieve(RetrieveUserContext request);

    List<UserDto> retrieve();

    UserDto update(UpdateUserContext request);

    void delete(DeleteUserContext request);

}
