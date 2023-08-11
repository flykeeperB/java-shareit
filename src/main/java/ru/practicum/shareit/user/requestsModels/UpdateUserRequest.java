package ru.practicum.shareit.user.requestsModels;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.dto.UserDto;

@Data
@Builder
public class UpdateUserRequest {
    private final Long targetUserId;
    private final UserDto userDto;
}
