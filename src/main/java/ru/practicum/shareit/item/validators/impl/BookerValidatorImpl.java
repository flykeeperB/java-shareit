package ru.practicum.shareit.item.validators.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.requestsModels.BasicItemRequest;
import ru.practicum.shareit.item.validators.BookerValidator;

import java.util.List;

@Component
public class BookerValidatorImpl implements BookerValidator {

    @Override
    public void validate(BasicItemRequest request, List<BookingDto> successfulBookings) {
        for (BookingDto successfulBooking : successfulBookings) {
            if (successfulBooking.getBooker().getId().equals(request.getSharerUserId())) {
                return;
            }
        }

        throw new ValidationException("вещь не бронировалась");
    }

}
