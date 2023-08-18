package ru.practicum.shareit.request.mapping;

import ru.practicum.shareit.request.dto.ExtraItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ToExtraItemRequestDtoListMapper {

    List<ExtraItemRequestDto> map(List<ItemRequest> source);

}
