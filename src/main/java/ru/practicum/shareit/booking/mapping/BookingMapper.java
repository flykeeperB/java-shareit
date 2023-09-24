package ru.practicum.shareit.booking.mapping;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingExtraDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface BookingMapper {

    BookingDto mapToBookingDto(Booking source);

    BookingDto mapToBookingDto(Booking source, BookingDto target);

    BookingExtraDto mapToBookingExtraDto(Booking source);

    List<BookingExtraDto> mapToBookingExtraDto(List<Booking> source);

    Booking mapToBooking(BookingExtraDto source, User sharerUser, Item item);

}
