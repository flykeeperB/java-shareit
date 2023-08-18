package ru.practicum.shareit.booking.validators.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.contexts.ApproveBookingContext;
import ru.practicum.shareit.booking.validators.AlreadyApprovedBookingValidator;
import ru.practicum.shareit.exception.ValidationException;

@Component
public class AlreadyApprovedBookingValidatorImpl implements AlreadyApprovedBookingValidator {

    @Override
    public void validate(ApproveBookingContext context) {
        if (context.getBooking().getStatus().equals(BookingStatus.APPROVED)
                && context.getIsApproved()) {
            throw new ValidationException("бронь уже была одобрена");
        }
    }

}
