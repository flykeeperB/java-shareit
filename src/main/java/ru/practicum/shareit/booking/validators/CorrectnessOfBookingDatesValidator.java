package ru.practicum.shareit.booking.validators;

import ru.practicum.shareit.booking.contexts.CreateBookingContext;

public interface CorrectnessOfBookingDatesValidator {
    void validate(CreateBookingContext context);
}
