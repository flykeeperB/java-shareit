package ru.practicum.shareit.booking.validators.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.contexts.BasicBookingContext;
import ru.practicum.shareit.booking.validators.OwnerOfBookedItemValidator;
import ru.practicum.shareit.exception.NotFoundException;

@Component
public class OwnerOfBookedItemValidatorImpl implements OwnerOfBookedItemValidator {

    @Override
    public void validate(BasicBookingContext context) {
        Long userId = context.getSharerUserId();

        if (!context.getBooking().getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException("пользователь - не владелец арендуемой вещи");
        }
    }

}
