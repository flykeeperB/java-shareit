package ru.practicum.shareit.user.validators;

import ru.practicum.shareit.user.dto.UserDto;

public interface UserNotBlankNameValidator {
    void validate(UserDto userDto);
}
