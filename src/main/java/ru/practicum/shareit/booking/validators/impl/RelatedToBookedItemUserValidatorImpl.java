package ru.practicum.shareit.booking.validators.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.contexts.BasicBookingContext;
import ru.practicum.shareit.booking.validators.RelatedToBookedItemUserValidator;
import ru.practicum.shareit.exception.NotFoundException;

@Component
public class RelatedToBookedItemUserValidatorImpl implements RelatedToBookedItemUserValidator {

    @Override
    public void validate(BasicBookingContext context) {
        Long userId = context.getSharerUserId();

        if (!context.getBooking().getBooker().getId().equals(userId) &&
                !context.getBooking().getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException("пользователь - не арендатор и не владелец вещи");
        }
    }

}
