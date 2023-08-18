package ru.practicum.shareit.booking.validators;

import ru.practicum.shareit.booking.contexts.ApproveBookingContext;

public interface AlreadyApprovedBookingValidator {
    void validate(ApproveBookingContext context);
}
