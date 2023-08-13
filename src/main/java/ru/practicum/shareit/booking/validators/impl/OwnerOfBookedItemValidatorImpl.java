package ru.practicum.shareit.booking.validators.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.requestsModels.SharerUserIdRequest;
import ru.practicum.shareit.booking.validators.OwnerOfBookedItemValidator;
import ru.practicum.shareit.exception.NotFoundException;

@Component
public class OwnerOfBookedItemValidatorImpl implements OwnerOfBookedItemValidator {

    @Override
    public void validate(SharerUserIdRequest request, Booking booking) {
        Long userId = request.getSharerUserId();

        if (!booking.getItem().getOwner().getId().equals(userId)) {
            throw new NotFoundException("пользователь - не владелец арендуемой вещи");
        }
    }

}
