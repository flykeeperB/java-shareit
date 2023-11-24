package ru.practicum.shareit.request.mapping.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.mapping.ItemMapper;
import ru.practicum.shareit.request.dto.ExtraItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapping.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemRequestMapperImpl implements ItemRequestMapper {

    @Override
    public ItemRequestDto mapToItemRequestDto(ItemRequest source) {
        return mapToItemRequestDto(source, new ItemRequestDto());
    }

    @Override
    public ItemRequestDto mapToItemRequestDto(ItemRequest source, ItemRequestDto target) {
        target.setId(source.getId());
        target.setDescription(source.getDescription());
        target.setCreated(source.getCreated());

        return target;
    }

    @Override
    public ItemRequest mapToItemRequest(ItemRequestDto source, User sharerUser) {
        ItemRequest itemRequest = new ItemRequest();

        itemRequest.setDescription(source.getDescription());
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequestor(sharerUser);

        return itemRequest;
    }

    @Override
    public ExtraItemRequestDto mapToExtraItemRequestDto(ItemRequest itemRequest) {

        ExtraItemRequestDto result = new ExtraItemRequestDto();

        mapToItemRequestDto(itemRequest, result);

        return result;
    }

    @Override
    public List<ExtraItemRequestDto> mapToExtraItemRequestDto(List<ItemRequest> source) {
        return source
                .stream()
                .map(this::mapToExtraItemRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ExtraItemRequestDto> mapToExtraItemRequestDto(List<ItemRequest> source, ItemMapper itemMapper) {
        List<ExtraItemRequestDto> result = new ArrayList<>();
        for (ItemRequest itemRequest : source) {
            ExtraItemRequestDto itemExtraDto = mapToExtraItemRequestDto(itemRequest);
            itemExtraDto.setItems(itemMapper.mapToItemDto(itemRequest.getItems()));
            result.add(itemExtraDto);
        }

        return result;
    }
}
