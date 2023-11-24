package ru.practicum.shareit.user.contexts;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class UpdateUserContext extends BasicUserContext {
    private final Long targetUserId;
}
