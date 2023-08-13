package ru.practicum.shareit.booking.validators;

import ru.practicum.shareit.booking.requestsModels.CreateBookingRequest;

public interface CorrectnessOfBookingDatesValidator {
    void validate(CreateBookingRequest request);
}
