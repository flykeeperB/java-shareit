package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.requestsModels.CreateUserRequest;
import ru.practicum.shareit.user.requestsModels.DeleteUserRequest;
import ru.practicum.shareit.user.requestsModels.RetrieveUserRequest;
import ru.practicum.shareit.user.requestsModels.UpdateUserRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserDto create(CreateUserRequest request);

    UserDto retrieve(RetrieveUserRequest request);

    List<UserDto> retrieve();

    List<UserDto> retrieve(List<Long> ids, Optional<Long> userId);

    UserDto update(UpdateUserRequest request);

    void delete(DeleteUserRequest request);

    User retrieve(Long id);

}
