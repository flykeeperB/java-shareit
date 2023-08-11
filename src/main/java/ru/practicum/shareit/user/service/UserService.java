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

    UserDto create(CreateUserRequest createUserRequest);

    UserDto retrieve(RetrieveUserRequest retrieveUserRequest);

    List<UserDto> retrieve();

    List<UserDto> retrieve(List<Long> ids, Optional<Long> userId);

    UserDto update(UpdateUserRequest updateUserRequest);

    void delete(DeleteUserRequest deleteUserRequest);

    User retrieve(Long id);

}
