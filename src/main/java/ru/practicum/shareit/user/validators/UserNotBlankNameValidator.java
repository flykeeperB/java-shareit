package ru.practicum.shareit.user.validators;

import ru.practicum.shareit.user.contexts.BasicUserContext;

public interface UserNotBlankNameValidator {
    void validate(BasicUserContext context);
}
