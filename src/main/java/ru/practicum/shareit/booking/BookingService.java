package ru.practicum.shareit.booking;

import ru.practicum.shareit.Service;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BookingService extends Service<Booking, BookingValidator> {

    Booking approve(Long id, Optional<Long> userId, Boolean isApproved);

    List<Booking> retrieveForBooker(Optional<Long> userId, State state);

    List<Booking> retrieveComplitedForBooker(Optional<Long> userId, State state);

    List<Booking> retrieveForItemsOwner(Optional<Long> userId, State state);

    Map<TypesOfBookingConnectionToItem, Booking> retrieveForItem(Long itemId);
}
