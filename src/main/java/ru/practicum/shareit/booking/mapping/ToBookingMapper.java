package ru.practicum.shareit.booking.mapping;

import ru.practicum.shareit.booking.contexts.CreateBookingContext;
import ru.practicum.shareit.booking.model.Booking;

public interface ToBookingMapper {

    Booking map(CreateBookingContext context);

}
