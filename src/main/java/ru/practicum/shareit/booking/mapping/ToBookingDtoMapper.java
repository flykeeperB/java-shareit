package ru.practicum.shareit.booking.mapping;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

public interface ToBookingDtoMapper {

    BookingDto map(Booking source);

    BookingDto map(Booking source, BookingDto target);

}
