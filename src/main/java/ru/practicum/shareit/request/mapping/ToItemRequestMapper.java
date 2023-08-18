package ru.practicum.shareit.request.mapping;

import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.contexts.CreateItemRequestContext;

public interface ToItemRequestMapper {

    ItemRequest map(CreateItemRequestContext context);

}
