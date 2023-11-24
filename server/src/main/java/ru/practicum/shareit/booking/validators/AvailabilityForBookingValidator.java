package ru.practicum.shareit.booking.validators;

import ru.practicum.shareit.item.model.Item;

public interface AvailabilityForBookingValidator {
    void validate(Long sharerUserId, Item item);
}
