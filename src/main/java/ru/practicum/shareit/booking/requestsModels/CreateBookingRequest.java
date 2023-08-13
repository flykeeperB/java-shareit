package ru.practicum.shareit.booking.requestsModels;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.booking.dto.BookingDto;

@Data
@SuperBuilder
public class CreateBookingRequest extends SharerUserIdRequest {
    private final BookingDto bookingDto;
}
