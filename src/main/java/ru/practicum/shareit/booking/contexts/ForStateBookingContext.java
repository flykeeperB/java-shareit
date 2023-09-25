package ru.practicum.shareit.booking.contexts;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.core.contexts.SharerUserContext;

@Data
@SuperBuilder
public class ForStateBookingContext extends SharerUserContext {
    private final State state;
    private final Integer from;
    private final Integer size;
}
