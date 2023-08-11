package ru.practicum.shareit.item.validators;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.requestsModels.BasicItemRequest;

public interface OnlyItemOwnerValidator {
    void Validate(BasicItemRequest request, Item item);
}
