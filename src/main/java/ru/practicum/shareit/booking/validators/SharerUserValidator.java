package ru.practicum.shareit.booking.validators;

import ru.practicum.shareit.booking.requestsModels.SharerUserIdRequest;

public interface SharerUserValidator {
    void validate(SharerUserIdRequest request);
}
