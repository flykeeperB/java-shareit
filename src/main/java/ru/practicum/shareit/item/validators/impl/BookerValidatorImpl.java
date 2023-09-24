package ru.practicum.shareit.item.validators.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.validators.BookerValidator;

import java.util.List;

@Component
public class BookerValidatorImpl implements BookerValidator {

    @Override
    public void validate(Long sharerUserId, List<Booking> successfulBookings) {
        for (Booking successfulBooking : successfulBookings) {
            if (successfulBooking.getBooker().getId().equals(sharerUserId)) {
                return;
            }
        }

        throw new ValidationException("вещь не бронировалась");
    }

}
