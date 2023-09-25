package ru.practicum.shareit.booking.validators;

import ru.practicum.shareit.booking.model.Booking;

public interface RelatedToBookedItemUserValidator {
    void validate(Long sharerUserId, Booking booking);
}
