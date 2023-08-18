package ru.practicum.shareit.core.validators.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.core.contexts.SharerUserContext;
import ru.practicum.shareit.core.validators.SharerUserValidator;
import ru.practicum.shareit.exception.NotFoundException;

@Component
@RequiredArgsConstructor
public class SharerUserValidatorImpl implements SharerUserValidator {

    @Override
    public void validate(SharerUserContext context) {
        if (context.getSharerUser() == null) {
            throw new NotFoundException("пользователь не найден");
        }
    }
}
