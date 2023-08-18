package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingExtraDto;
import ru.practicum.shareit.booking.contexts.*;

import java.util.List;

public interface ControllerBookingService {

    BookingExtraDto create(CreateBookingContext context);

    BookingExtraDto retrieve(BasicBookingContext context);

    List<BookingExtraDto> retrieve();

    void delete(BasicBookingContext context);

    BookingExtraDto approve(ApproveBookingContext context);

    List<BookingExtraDto> retrieveForBooker(ForStateBookingContext context);

    List<BookingExtraDto> retrieveForItemsOwner(ForStateBookingContext context);

}
