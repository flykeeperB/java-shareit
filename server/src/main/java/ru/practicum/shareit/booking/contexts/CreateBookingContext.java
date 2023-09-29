package ru.practicum.shareit.booking.contexts;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.booking.dto.BookingExtraDto;
import ru.practicum.shareit.core.contexts.SharerUserContext;

@Data
@SuperBuilder
public class CreateBookingContext extends SharerUserContext {
    private final BookingExtraDto bookingExtraDto;
}
