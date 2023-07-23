package ru.practicum.shareit.booking;

import ru.practicum.shareit.ShareItRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends ShareItRepository<Booking> {

    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);

    List<Booking> findByBookerIdAndStartAfterOrderByStartDesc(Long bookerId, LocalDateTime now);

    List<Booking> findByBookerIdAndEndBeforeOrderByStartDesc(Long bookerId, LocalDateTime now);

    List<Booking> findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long bookerId,
                                                                          LocalDateTime start, LocalDateTime end);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId, BookingStatus status);

    List<Booking> findByItemOwnerIdOrderByStartDesc(Long ownerId);

    List<Booking> findByItemOwnerIdAndStartAfterOrderByStartDesc(Long ownerId, LocalDateTime now);

    List<Booking> findByItemOwnerIdAndEndBeforeOrderByStartDesc(Long ownerId, LocalDateTime now);

    List<Booking> findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long ownerId,
                                                                             LocalDateTime start, LocalDateTime end);

    List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(Long ownerId, BookingStatus status);


    List<Booking> findFirstByItemIdAndStatusAndStartAfterOrderByStart(Long itemId,
                                                                      BookingStatus status,
                                                                      LocalDateTime start);

    List<Booking> findByItemIdAndStatusAndStartBeforeOrderByEndDesc(Long itemId,
                                                                    BookingStatus status,
                                                                    LocalDateTime end);

    List<Booking> findByBookerIdAndStatusAndStartBefore(Long bookerId,
                                                        BookingStatus status,
                                                        LocalDateTime end);

    List<Booking> findByBookerIdAndStatusAndEndBefore(Long bookerId,
                                                      BookingStatus status,
                                                      LocalDateTime end);

    List<Booking> findByItemOwnerIdAndStatusAndStartBefore(Long bookerId,
                                                           BookingStatus status,
                                                           LocalDateTime end);

    List<Booking> findByItemOwnerIdAndStatusAndEndBefore(Long bookerId,
                                                         BookingStatus status,
                                                         LocalDateTime end);
}
