package ru.practicum.shareit.booking.requestsModels;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ApproveBookingRequest extends BasicBookingRequest {
    private final Boolean isApproved;
}
