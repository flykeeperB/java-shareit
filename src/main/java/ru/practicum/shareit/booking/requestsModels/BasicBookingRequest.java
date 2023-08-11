package ru.practicum.shareit.booking.requestsModels;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class BasicBookingRequest extends SharerUserIdRequest {
    private final Long targetBookingId;
}
