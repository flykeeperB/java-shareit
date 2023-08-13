package ru.practicum.shareit.user.requestsModels;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteUserRequest {
    private final Long targetUserId;
}
