package ru.practicum.shareit.booking.validators.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.validators.AvailabilityForBookingValidator;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;

@Component
public class AvailabilityForBookingValidatorImpl implements AvailabilityForBookingValidator {

    @Override
    public void validate(Long sharerUserId, Item item) {
        if (!item.getAvailable()) {
            throw new ValidationException("вещь недоступна для бронирования");
        }

        if (sharerUserId.equals(item.getOwner().getId())) {
            throw new NotFoundException("бессмысленное бронирование вещи её владельцем");
        }
    }
}
