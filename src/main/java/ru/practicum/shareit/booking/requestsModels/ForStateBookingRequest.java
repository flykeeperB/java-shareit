package ru.practicum.shareit.booking.requestsModels;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.booking.dto.State;

@Data
@SuperBuilder
public class ForStateBookingRequest extends SharerUserIdRequest {
    private final State state;
}
