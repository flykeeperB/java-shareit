package ru.practicum.shareit.request.mapping;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

public interface ToItemRequestDtoMapper {

    ItemRequestDto map(ItemRequest itemRequest);

    ItemRequestDto map(ItemRequest itemRequest, ItemRequestDto target);

}
