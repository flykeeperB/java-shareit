package ru.practicum.shareit.item.mapping.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.mapping.ToBookingDtoMapper;
import ru.practicum.shareit.item.dto.ItemExtraDto;
import ru.practicum.shareit.item.mapping.ToItemDtoMapper;
import ru.practicum.shareit.item.mapping.ToItemExtraDtoMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ToItemExtraDtoMapperImpl implements ToItemExtraDtoMapper {

    @Lazy
    private final ToBookingDtoMapper toBookingDtoMapper;

    @Lazy
    private final ToItemDtoMapper toItemDtoMapper;

    @Override
    public ItemExtraDto map(Item source) {
        return map(source, new ItemExtraDto());
    }

    @Override
    public ItemExtraDto map(Item source, ItemExtraDto target) {
        if (source == null) {
            return null;
        }

        toItemDtoMapper.map(source, target);
        target.setNextBooking(toBookingDtoMapper.map(source.getNextBooking()));
        target.setLastBooking(toBookingDtoMapper.map(source.getLastBooking()));
        return target;
    }

    @Override
    public List<ItemExtraDto> map(List<Item> source) {
        return source
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }
}
