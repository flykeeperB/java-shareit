package ru.practicum.shareit.item.validators;

import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookerValidator {
    void validate(Long sharerUserId, List<Booking> successfulBookings);
}
