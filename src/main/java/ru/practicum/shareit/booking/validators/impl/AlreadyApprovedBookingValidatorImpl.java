package ru.practicum.shareit.booking.validators.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.requestsModels.ApproveBookingRequest;
import ru.practicum.shareit.booking.validators.AlreadyApprovedBookingValidator;
import ru.practicum.shareit.exception.ValidationException;

@Component
public class AlreadyApprovedBookingValidatorImpl implements AlreadyApprovedBookingValidator {

    @Override
    public void Validate(ApproveBookingRequest request, Booking booking) {
        if (booking.getStatus().equals(BookingStatus.APPROVED)
                && request.getIsApproved()) {
            throw new ValidationException("бронь уже была одобрена");
        }
    }

}
