package ru.practicum.shareit.booking.mapping.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingExtraDto;
import ru.practicum.shareit.booking.mapping.ToBookingExtraDtoMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapping.ToItemDtoMapper;
import ru.practicum.shareit.user.mapping.ToUserDtoMapper;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ToBookingExtraDtoMapperImpl implements ToBookingExtraDtoMapper {

    @Lazy
    private final ToBookingDtoMapperImpl toBookingDtoMapper;

    @Lazy
    private final ToUserDtoMapper toUserDtoMapper;

    @Lazy
    private final ToItemDtoMapper toItemDtoMapper;

    @Override
    public BookingExtraDto map(Booking source) {
        return map(source, new BookingExtraDto());
    }

    @Override
    public BookingExtraDto map(Booking source, BookingExtraDto target) {
        if (source == null) {
            return null;
        }

        toBookingDtoMapper.map(source, target);
        target.setStatus(source.getStatus());
        target.setBooker(toUserDtoMapper.map(source.getBooker()));
        target.setItem(toItemDtoMapper.map(source.getItem()));

        if (source.getItem() != null) {
            target.setItemId(source.getItem().getId());
        }

        return target;
    }

    @Override
    public List<BookingExtraDto> map(List<Booking> source) {
        return source
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }
}
