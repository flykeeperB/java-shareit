package ru.practicum.shareit.booking.validators;

import ru.practicum.shareit.booking.dto.BookingDto;

public interface CorrectnessOfBookingDatesValidator {
    void validate(BookingDto bookingDto);
}
