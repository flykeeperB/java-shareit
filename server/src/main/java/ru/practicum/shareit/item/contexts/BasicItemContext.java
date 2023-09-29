package ru.practicum.shareit.item.contexts;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.core.contexts.SharerUserContext;

@Data
@SuperBuilder
public class BasicItemContext extends SharerUserContext {
    private final Long targetItemId;
}
