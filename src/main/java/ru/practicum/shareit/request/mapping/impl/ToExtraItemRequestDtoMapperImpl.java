package ru.practicum.shareit.request.mapping.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.mapping.ToItemDtoMapper;
import ru.practicum.shareit.request.dto.ExtraItemRequestDto;
import ru.practicum.shareit.request.mapping.ToExtraItemRequestDtoMapper;
import ru.practicum.shareit.request.mapping.ToItemRequestDtoMapper;
import ru.practicum.shareit.request.model.ItemRequest;

@Component
@RequiredArgsConstructor
public class ToExtraItemRequestDtoMapperImpl implements ToExtraItemRequestDtoMapper {

    private final ToItemRequestDtoMapper toItemRequestDtoMapper;
    private final ToItemDtoMapper toItemDtoMapper;

    @Override
    public ExtraItemRequestDto map(ItemRequest itemRequest) {

        ExtraItemRequestDto result = new ExtraItemRequestDto();

        toItemRequestDtoMapper.map(itemRequest, result);

        result.setItems(toItemDtoMapper.map(itemRequest.getItems()));

        return result;
    }
}
