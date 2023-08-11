package ru.practicum.shareit.user.requestsModels;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class CreateUserRequest {
    @NotNull
    private final UserDto userDto;
}
