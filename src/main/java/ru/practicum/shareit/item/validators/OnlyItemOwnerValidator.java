package ru.practicum.shareit.item.validators;

import ru.practicum.shareit.item.model.Item;

public interface OnlyItemOwnerValidator {
    void validate(Long sharerUserId, Item oldItem);
}
