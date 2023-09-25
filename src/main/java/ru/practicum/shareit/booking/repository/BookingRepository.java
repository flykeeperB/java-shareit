package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId, Pageable pageable);

    List<Booking> findByBookerIdAndStartAfterOrderByStartDesc(Long bookerId, LocalDateTime now, Pageable pageable);

    List<Booking> findByBookerIdAndEndBeforeOrderByStartDesc(Long bookerId, LocalDateTime now, Pageable pageable);

    List<Booking> findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long bookerId,
                                                                          LocalDateTime start,
                                                                          LocalDateTime end,
                                                                          Pageable pageable);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long bookerId, BookingStatus status, Pageable pageable);

    List<Booking> findByItemOwnerIdOrderByStartDesc(Long ownerId, Pageable pageable);

    List<Booking> findByItemOwnerIdAndStartAfterOrderByStartDesc(Long ownerId, LocalDateTime now, Pageable pageable);

    List<Booking> findByItemOwnerIdAndEndBeforeOrderByStartDesc(Long ownerId, LocalDateTime now, Pageable pageable);

    List<Booking> findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long ownerId,
                                                                             LocalDateTime start,
                                                                             LocalDateTime end,
                                                                             Pageable pageable);

    List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(Long ownerId,
                                                             BookingStatus status,
                                                             Pageable pageable);


    List<Booking> findFirstByItemIdAndStatusAndStartAfterOrderByStart(Long itemId,
                                                                      BookingStatus status,
                                                                      LocalDateTime start);

    @Query("select bk " +
            "from Booking as bk " +
            "where bk.item.id in (?1) " +
            "and bk.status in ?2 " +
            "and bk.start < ?3 " +
            "order by bk.item.id, bk.start desc")
    List<Booking> findLastBookingsForItems(List<Long> itemId,
                                           BookingStatus status,
                                           LocalDateTime start);

    List<Booking> findByItemIdAndStatusAndStartBeforeOrderByEndDesc(Long itemId,
                                                                    BookingStatus status,
                                                                    LocalDateTime moment);

    @Query("select bk " +
            "from Booking as bk " +
            "where bk.item.id in (?1) " +
            "and bk.status in ?2 " +
            "and bk.start > ?3 " +
            "order by bk.item.id, bk.start desc")
    List<Booking> findNextBookingsForItems(List<Long> itemId,
                                           BookingStatus status,
                                           LocalDateTime moment);

    List<Booking> findByBookerIdAndStatusAndStartBefore(Long bookerId,
                                                        BookingStatus status,
                                                        LocalDateTime end,
                                                        Pageable pageable);

    List<Booking> findByBookerIdAndStatusAndEndBefore(Long bookerId,
                                                      BookingStatus status,
                                                      LocalDateTime end,
                                                      Pageable pageable);

    List<Booking> findByBookerIdAndStatusAndEndBefore(Long bookerId,
                                                      BookingStatus status,
                                                      LocalDateTime end);

    List<Booking> findByItemOwnerIdAndStatusAndStartBefore(Long bookerId,
                                                           BookingStatus status,
                                                           LocalDateTime end,
                                                           Pageable pageable);

    List<Booking> findByItemOwnerIdAndStatusAndEndBefore(Long bookerId,
                                                         BookingStatus status,
                                                         LocalDateTime end,
                                                         Pageable pageable);
}
