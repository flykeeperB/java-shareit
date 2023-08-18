package ru.practicum.shareit.core.contexts;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.user.model.User;

@Data
@SuperBuilder
public class SharerUserContext {
    private final Long sharerUserId;
    private User sharerUser;
}
