package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.impl.TypesOfBookingConnectionToItem;

import java.util.List;
import java.util.Map;

public interface ExternalBookingService {
    Map<TypesOfBookingConnectionToItem, Booking> retrieveForItem(Long itemId);

    Map<Long, Map<TypesOfBookingConnectionToItem, Booking>> retrieveForItems(List<Long> itemId);

    Booking retrieve(Long bookingId);

    List<Booking> retrieveSuccessfulBookings(Long bookerId);
}
