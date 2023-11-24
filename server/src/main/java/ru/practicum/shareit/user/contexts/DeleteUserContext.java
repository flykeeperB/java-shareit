package ru.practicum.shareit.user.contexts;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteUserContext {
    private final Long targetUserId;
}
