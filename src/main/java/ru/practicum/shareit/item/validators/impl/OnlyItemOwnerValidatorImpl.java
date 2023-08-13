package ru.practicum.shareit.item.validators.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.requestsModels.BasicItemRequest;
import ru.practicum.shareit.item.validators.OnlyItemOwnerValidator;

@Component
public class OnlyItemOwnerValidatorImpl implements OnlyItemOwnerValidator {

    @Override
    public void validate(BasicItemRequest request, Item item) {
        if (!item.getOwner().getId().equals(request.getSharerUserId())) {
            throw new AccessDeniedException("Недопустимо для невладельца");
        }
    }

}
