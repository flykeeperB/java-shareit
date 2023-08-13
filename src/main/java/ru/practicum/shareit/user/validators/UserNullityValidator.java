package ru.practicum.shareit.user.validators;

import ru.practicum.shareit.user.model.User;

import java.util.Optional;

public interface UserNullityValidator {
    void validate(Optional<User> user);
}
