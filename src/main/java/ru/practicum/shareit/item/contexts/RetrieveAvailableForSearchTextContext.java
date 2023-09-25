package ru.practicum.shareit.item.contexts;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.core.contexts.SharerUserContext;

@Data
@SuperBuilder
public class RetrieveAvailableForSearchTextContext extends SharerUserContext {
    private final String searchText;
    private final Integer from;
    private final Integer size;
}
