package ru.practicum.shareit.request.validators;

import ru.practicum.shareit.request.dto.ItemRequestDto;

public interface NotBlankDescriptionOfItemRequestValidator {

    void validate(ItemRequestDto itemRequestDto);

}
