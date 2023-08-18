package ru.practicum.shareit.item.contexts;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.core.contexts.SharerUserContext;

@Data
@SuperBuilder
public class RetrieveItemForOwnerContext extends SharerUserContext {
    private final Long owner;
    private final Integer from;
    private final Integer size;
}
