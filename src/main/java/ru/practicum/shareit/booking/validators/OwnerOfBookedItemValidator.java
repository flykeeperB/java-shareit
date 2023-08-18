package ru.practicum.shareit.booking.validators;

import ru.practicum.shareit.booking.contexts.BasicBookingContext;

public interface OwnerOfBookedItemValidator {
    void validate(BasicBookingContext context);
}
