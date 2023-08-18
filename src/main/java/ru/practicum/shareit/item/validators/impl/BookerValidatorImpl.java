package ru.practicum.shareit.item.validators.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.contexts.CreateCommentContext;
import ru.practicum.shareit.item.validators.BookerValidator;

@Component
public class BookerValidatorImpl implements BookerValidator {

    @Override
    public void validate(CreateCommentContext context) {
        for (Booking successfulBooking : context.getSuccessfulBookings()) {
            if (successfulBooking.getBooker().getId().equals(context.getSharerUserId())) {
                return;
            }
        }

        throw new ValidationException("вещь не бронировалась");
    }

}
