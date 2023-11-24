package ru.practicum.shareit.booking.validators;

import ru.practicum.shareit.booking.model.Booking;

public interface AlreadyApprovedBookingValidator {
    void validate(Booking booking);
}
