package ru.practicum.shareit.booking.validators;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.requestsModels.ApproveBookingRequest;

public interface AlreadyApprovedBookingValidator {
    void validate(ApproveBookingRequest request, Booking booking);
}
