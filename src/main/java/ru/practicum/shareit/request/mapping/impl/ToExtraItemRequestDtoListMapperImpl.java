package ru.practicum.shareit.request.mapping.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.dto.ExtraItemRequestDto;
import ru.practicum.shareit.request.mapping.ToExtraItemRequestDtoListMapper;
import ru.practicum.shareit.request.mapping.ToExtraItemRequestDtoMapper;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ToExtraItemRequestDtoListMapperImpl implements ToExtraItemRequestDtoListMapper {

    private final ToExtraItemRequestDtoMapper toExtraItemRequestDtoMapper;

    @Override
    public List<ExtraItemRequestDto> map(List<ItemRequest> source) {
        return source
                .stream()
                .map(toExtraItemRequestDtoMapper::map)
                .collect(Collectors.toList());
    }
}
