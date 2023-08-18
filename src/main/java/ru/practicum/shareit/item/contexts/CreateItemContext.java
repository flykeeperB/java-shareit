package ru.practicum.shareit.item.contexts;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.model.ItemRequest;

@Data
@SuperBuilder
public class CreateItemContext extends BasicItemContext {
    private final ItemDto itemDto;
    private ItemRequest itemRequest;
}
