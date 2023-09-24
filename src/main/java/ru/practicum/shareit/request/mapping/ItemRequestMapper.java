package ru.practicum.shareit.request.mapping;

import ru.practicum.shareit.item.mapping.ItemMapper;
import ru.practicum.shareit.request.dto.ExtraItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRequestMapper {

    ItemRequestDto mapToItemRequestDto(ItemRequest source);

    ItemRequestDto mapToItemRequestDto(ItemRequest source, ItemRequestDto target);

    ItemRequest mapToItemRequest(ItemRequestDto source, User sharerUser);

    ExtraItemRequestDto mapToExtraItemRequestDto(ItemRequest itemRequest);

    List<ExtraItemRequestDto> mapToExtraItemRequestDto(List<ItemRequest> source);

    List<ExtraItemRequestDto> mapToExtraItemRequestDto(List<ItemRequest> source, ItemMapper itemMapper);

}
