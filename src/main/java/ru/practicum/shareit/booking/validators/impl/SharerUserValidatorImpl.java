package ru.practicum.shareit.booking.validators.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.requestsModels.SharerUserIdRequest;
import ru.practicum.shareit.booking.validators.SharerUserValidator;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.model.User;

@Component
@RequiredArgsConstructor
public class SharerUserValidatorImpl implements SharerUserValidator {

    @Lazy
    private final UserService userService;

    @Override
    public void Validate(SharerUserIdRequest request) {
        User user = userService.retrieve(request.getSharerUserId());
        if (user == null) {
            throw new NotFoundException("пользователь не найден");
        }
    }
}
