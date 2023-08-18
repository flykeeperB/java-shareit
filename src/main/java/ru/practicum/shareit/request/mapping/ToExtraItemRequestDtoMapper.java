package ru.practicum.shareit.request.mapping;

import ru.practicum.shareit.request.dto.ExtraItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

public interface ToExtraItemRequestDtoMapper {

    ExtraItemRequestDto map(ItemRequest itemRequest);

}
