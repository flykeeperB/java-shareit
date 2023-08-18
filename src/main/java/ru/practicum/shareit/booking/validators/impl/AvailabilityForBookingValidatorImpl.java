package ru.practicum.shareit.booking.validators.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.contexts.CreateBookingContext;
import ru.practicum.shareit.booking.validators.AvailabilityForBookingValidator;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;

@Component
public class AvailabilityForBookingValidatorImpl implements AvailabilityForBookingValidator {

    @Override
    public void validate(CreateBookingContext context) {
        if (!context.getItem().getAvailable()) {
            throw new ValidationException("вещь недоступна для бронирования");
        }

        if (context.getSharerUserId().equals(context.getItem().getOwner().getId())) {
            throw new NotFoundException("бессмысленное бронирование вещи её владельцем");
        }
    }
}
