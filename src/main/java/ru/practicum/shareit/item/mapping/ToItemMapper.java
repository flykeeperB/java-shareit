package ru.practicum.shareit.item.mapping;

import ru.practicum.shareit.item.contexts.CreateItemContext;
import ru.practicum.shareit.item.model.Item;

public interface ToItemMapper {

    Item map(CreateItemContext context);

}
