package ru.practicum.shareit.item.validators.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.item.contexts.UpdateItemContext;
import ru.practicum.shareit.item.validators.OnlyItemOwnerValidator;

@Component
public class OnlyItemOwnerValidatorImpl implements OnlyItemOwnerValidator {

    @Override
    public void validate(UpdateItemContext context) {
        if (!context.getOldItem().getOwner().getId().equals(context.getSharerUserId())) {
            throw new AccessDeniedException("Недопустимо для невладельца");
        }
    }

}
