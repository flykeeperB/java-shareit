package ru.practicum.shareit.request.validators.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.validators.NotBlankDescriptionOfItemRequestValidator;

@Component
public class NotBlankDescriptionOfItemRequestValidatorImpl implements NotBlankDescriptionOfItemRequestValidator {

    @Override
    public void validate(ItemRequestDto itemRequestDto) {
        if ((itemRequestDto.getDescription() == null)
                || itemRequestDto.getDescription().isEmpty()) {
            throw new ValidationException("не указано содержание запроса");
        }
    }

}
