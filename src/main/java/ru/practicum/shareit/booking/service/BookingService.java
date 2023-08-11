package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.requestsModels.*;

import java.util.List;
import java.util.Map;

public interface BookingService {

    BookingDto create(CreateBookingRequest createBookingRequest);

    BookingDto retrieve(BasicBookingRequest basicBookingRequest);

    List<BookingDto> retrieve();

    void delete(BasicBookingRequest request);

    BookingDto approve(ApproveBookingRequest approveBookingRequest);

    List<BookingDto> retrieveForBooker(ForStateBookingRequest request);

    List<BookingDto> retrieveForItemsOwner(ForStateBookingRequest request);

    Map<TypesOfBookingConnectionToItem, Booking> retrieveForItem(Long itemId);

    Map<Long, Map<TypesOfBookingConnectionToItem, Booking>> retrieveForItems(List<Long> itemId);

    Booking retrieve(Long bookingId);

}
