package ru.practicum.shareit.booking.validators;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.requestsModels.SharerUserIdRequest;

public interface OwnerOfBookedItemValidator {
    void Validate(SharerUserIdRequest request, Booking booking);
}
