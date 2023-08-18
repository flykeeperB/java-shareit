package ru.practicum.shareit.booking.mapping.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapping.ToBookingDtoMapper;
import ru.practicum.shareit.booking.model.Booking;

@Component
public class ToBookingDtoMapperImpl implements ToBookingDtoMapper {

    @Override
    public BookingDto map(Booking source) {
        return map(source, new BookingDto());
    }

    @Override
    public BookingDto map(Booking source, BookingDto target) {
        if (source == null) {
            return null;
        }

        target.setId(source.getId());
        target.setStart(source.getStart());
        target.setEnd(source.getEnd());
        if (source.getBooker() != null) {
            target.setBookerId(source.getBooker().getId());
        }

        return target;
    }
}
