package ru.practicum.shareit.request.validators.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.request.contexts.CreateItemRequestContext;
import ru.practicum.shareit.request.validators.NotBlankDescriptionOfItemRequestValidator;

@Component
public class NotBlankDescriptionOfItemRequestValidatorImpl implements NotBlankDescriptionOfItemRequestValidator {

    @Override
    public void validate(CreateItemRequestContext request) {
        if ((request.getCreateItemRequestDto().getDescription() == null)
                || request.getCreateItemRequestDto().getDescription().isEmpty()) {
            throw new ValidationException("не указано содержание запроса");
        }
    }

}
