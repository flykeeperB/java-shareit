package ru.practicum.shareit.booking.validators;

import ru.practicum.shareit.booking.dto.BookingExtraDto;

public interface CorrectnessOfBookingDatesValidator {
    void validate(BookingExtraDto bookingExtraDto);
}
