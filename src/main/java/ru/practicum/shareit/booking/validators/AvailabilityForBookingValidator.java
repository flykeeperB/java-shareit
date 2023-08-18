package ru.practicum.shareit.booking.validators;

import ru.practicum.shareit.booking.contexts.CreateBookingContext;

public interface AvailabilityForBookingValidator {
    void validate(CreateBookingContext context);
}
