package ru.practicum.shareit.request.mapping.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapping.ToItemRequestDtoMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.mapping.ToUserDtoMapper;

@Component
@RequiredArgsConstructor
public class ToItemRequestDtoMapperImpl implements ToItemRequestDtoMapper {

    private final ToUserDtoMapper toUserDtoMapper;

    @Override
    public ItemRequestDto map(ItemRequest itemRequest) {
        return map(itemRequest, new ItemRequestDto());
    }

    @Override
    public ItemRequestDto map(ItemRequest itemRequest, ItemRequestDto target) {
        target.setId(itemRequest.getId());
        target.setDescription(itemRequest.getDescription());
        target.setCreated(itemRequest.getCreated());
        target.setRequestor(toUserDtoMapper.map(itemRequest.getRequestor()));

        return target;
    }
}
