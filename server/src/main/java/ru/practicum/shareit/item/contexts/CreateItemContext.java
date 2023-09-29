package ru.practicum.shareit.item.contexts;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.item.dto.ItemDto;

@Data
@SuperBuilder
public class CreateItemContext extends BasicItemContext {
    private final ItemDto itemDto;
}
