package ru.practicum.shareit.booking.contexts;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.core.contexts.SharerUserContext;

@Data
@SuperBuilder
public class BasicBookingContext extends SharerUserContext {
    private final Long targetBookingId;
    private Booking booking;
}
