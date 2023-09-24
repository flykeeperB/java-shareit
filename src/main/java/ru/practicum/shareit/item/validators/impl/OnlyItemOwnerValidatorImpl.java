package ru.practicum.shareit.item.validators.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.validators.OnlyItemOwnerValidator;

@Component
public class OnlyItemOwnerValidatorImpl implements OnlyItemOwnerValidator {

    @Override
    public void validate(Long sharerUserId, Item oldItem) {
        if (!oldItem.getOwner().getId().equals(sharerUserId)) {
            throw new AccessDeniedException("Недопустимо для невладельца");
        }
    }

}
