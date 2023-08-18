package ru.practicum.shareit.request.validators;

import ru.practicum.shareit.request.contexts.CreateItemRequestContext;

public interface NotBlankDescriptionOfItemRequestValidator {

    void validate(CreateItemRequestContext request);

}
