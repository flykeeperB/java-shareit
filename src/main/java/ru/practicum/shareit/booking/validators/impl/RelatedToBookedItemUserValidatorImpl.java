package ru.practicum.shareit.booking.validators.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.requestsModels.SharerUserIdRequest;
import ru.practicum.shareit.booking.validators.RelatedToBookedItemUserValidator;
import ru.practicum.shareit.exception.NotFoundException;

@Component
public class RelatedToBookedItemUserValidatorImpl implements RelatedToBookedItemUserValidator {

    @Override
    public void validate(SharerUserIdRequest request, Booking booking) {
        Long userId = request.getSharerUserId();

        if (!booking.getBooker().getId().equals(userId) &&
                !booking.getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException("пользователь - не арендатор и не владелец вещи");
        }
    }

}
