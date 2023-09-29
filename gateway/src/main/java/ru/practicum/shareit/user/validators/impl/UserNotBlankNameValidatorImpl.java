package ru.practicum.shareit.user.validators.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.validators.UserNotBlankNameValidator;

@Component
public class UserNotBlankNameValidatorImpl implements UserNotBlankNameValidator {
    @Override
    public void validate(UserDto userDto) {
        if (userDto.getName() != null && userDto.getName().isEmpty()) {
            throw new ValidationException("имя пользователя не задано");
        }
    }
}