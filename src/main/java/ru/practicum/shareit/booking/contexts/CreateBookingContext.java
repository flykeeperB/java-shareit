package ru.practicum.shareit.booking.contexts;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.booking.dto.BookingExtraDto;
import ru.practicum.shareit.core.contexts.SharerUserContext;
import ru.practicum.shareit.item.model.Item;

@Data
@SuperBuilder
public class CreateBookingContext extends SharerUserContext {
    private final BookingExtraDto bookingExtraDto;
    private Item item;
}
