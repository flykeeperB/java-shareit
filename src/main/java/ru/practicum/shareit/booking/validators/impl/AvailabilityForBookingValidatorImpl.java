package ru.practicum.shareit.booking.validators.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.requestsModels.CreateBookingRequest;
import ru.practicum.shareit.booking.validators.AvailabilityForBookingValidator;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.model.Item;

@Component
@RequiredArgsConstructor
public class AvailabilityForBookingValidatorImpl implements AvailabilityForBookingValidator {

    @Lazy
    private final ItemService itemService;

    @Override
    public void validate(CreateBookingRequest request) {
        Item item = itemService.retrieve(request.getBookingDto().getItemId());
        if (!item.getAvailable()) {
            throw new ValidationException("вещь недоступна для бронирования");
        }

        if (request.getSharerUserId().equals(item.getOwner().getId())) {
            throw new NotFoundException("бессмысленное бронирование вещи её владельцем");
        }
    }
}
