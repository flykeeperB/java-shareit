package ru.practicum.shareit.booking.validators.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.validators.RelatedToBookedItemUserValidator;
import ru.practicum.shareit.exception.NotFoundException;

@Component
public class RelatedToBookedItemUserValidatorImpl implements RelatedToBookedItemUserValidator {

    @Override
    public void validate(Long sharerUserId, Booking booking) {

        if (!booking.getBooker().getId().equals(sharerUserId) &&
                !booking.getItem().getOwner().getId().equals(sharerUserId)) {
            throw new NotFoundException("пользователь - не арендатор и не владелец вещи");
        }
    }

}
