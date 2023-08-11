package ru.practicum.shareit.item.validators;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.requestsModels.BasicItemRequest;

import java.util.List;

public interface BookerValidator {
    void validate(BasicItemRequest request, List<BookingDto> successfulBookings);
}
