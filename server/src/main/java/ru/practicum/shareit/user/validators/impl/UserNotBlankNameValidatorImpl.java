package ru.practicum.shareit.user.validators.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.contexts.BasicUserContext;
import ru.practicum.shareit.user.validators.UserNotBlankNameValidator;

@Component
public class UserNotBlankNameValidatorImpl implements UserNotBlankNameValidator {
    @Override
    public void validate(BasicUserContext context) {
        if (context.getUserDto().getName().isEmpty()) {
            throw new ValidationException("имя пользователя не задано");
        }
    }
}