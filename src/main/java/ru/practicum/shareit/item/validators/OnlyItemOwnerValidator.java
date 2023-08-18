package ru.practicum.shareit.item.validators;

import ru.practicum.shareit.item.contexts.UpdateItemContext;

public interface OnlyItemOwnerValidator {
    void validate(UpdateItemContext context);
}
