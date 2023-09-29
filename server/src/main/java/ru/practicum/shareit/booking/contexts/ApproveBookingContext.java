package ru.practicum.shareit.booking.contexts;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ApproveBookingContext extends BasicBookingContext {
    private final Boolean isApproved;
}
