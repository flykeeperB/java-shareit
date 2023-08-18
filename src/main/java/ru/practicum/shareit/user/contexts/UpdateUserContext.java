package ru.practicum.shareit.user.contexts;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.dto.UserDto;

@Data
@Builder
public class UpdateUserContext {
    private final Long targetUserId;
    private final UserDto userDto;
}
