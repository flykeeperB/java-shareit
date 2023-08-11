package ru.practicum.shareit.user.validators.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.validators.UserNullityValidator;

import java.util.Optional;

@Component
public class UserNullityValidatorImpl implements UserNullityValidator {

    @Override
    public void Validate(Optional<User> user) {
        user.orElseThrow(() -> new NotFoundException("запис о пользователе не найдена"));
    }
}
