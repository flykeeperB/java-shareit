package ru.practicum.shareit.booking.mapping;

import ru.practicum.shareit.booking.dto.BookingExtraDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface ToBookingExtraDtoMapper {

    BookingExtraDto map(Booking source);

    BookingExtraDto map(Booking source, BookingExtraDto target);

    List<BookingExtraDto> map(List<Booking> source);

}
