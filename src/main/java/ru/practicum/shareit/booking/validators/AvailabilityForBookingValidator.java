package ru.practicum.shareit.booking.validators;

import ru.practicum.shareit.booking.requestsModels.CreateBookingRequest;

public interface AvailabilityForBookingValidator {
    void Validate(CreateBookingRequest request);
}
