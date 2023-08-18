package ru.practicum.shareit.booking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.mapping.ToBookingExtraDtoMapper;
import ru.practicum.shareit.booking.mapping.ToBookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingExtraDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.contexts.*;
import ru.practicum.shareit.booking.service.ControllerBookingService;
import ru.practicum.shareit.booking.service.ExternalBookingService;
import ru.practicum.shareit.booking.validators.*;
import ru.practicum.shareit.core.validators.SharerUserValidator;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.service.ExternalItemService;
import ru.practicum.shareit.user.service.ExternalUserService;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements ControllerBookingService, ExternalBookingService {

    private final BookingRepository bookingRepository;

    private final AvailabilityForBookingValidator availabilityForBookingValidator;
    private final CorrectnessOfBookingDatesValidator correctnessOfBookingDatesValidator;
    private final SharerUserValidator sharerUserValidator;
    private final RelatedToBookedItemUserValidator relatedToBookedItemUserValidator;
    private final OwnerOfBookedItemValidator ownerOfBookedItemValidator;
    private final AlreadyApprovedBookingValidator alreadyApprovedBookingValidator;

    private final ToBookingMapper toBookingMapper;
    private final ToBookingExtraDtoMapper toBookingExtraDtoMapper;

    @Lazy
    private final ExternalUserService userService;

    @Lazy
    private final ExternalItemService itemService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public BookingExtraDto create(CreateBookingContext context) {
        log.info("создание записи");

        context.setSharerUser(userService.retrieve(context.getSharerUserId()));

        sharerUserValidator.validate(context);

        context.setItem(itemService.retrieve(context.getBookingExtraDto().getItemId()));

        availabilityForBookingValidator.validate(context);
        correctnessOfBookingDatesValidator.validate(context);

        context.getBookingExtraDto().setBookerId(context.getSharerUserId());

        Booking booking = toBookingMapper.map(context);

        Booking newBooking = bookingRepository.save(booking);

        return toBookingExtraDtoMapper.map(newBooking);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<BookingExtraDto> retrieveForBooker(ForStateBookingContext context) {
        log.info("получение сведений о бронированиях для отдельного пользователя");

        Long bookerId = context.getSharerUserId();
        context.setSharerUser(userService.retrieve(bookerId));

        sharerUserValidator.validate(context);
        Pageable pageable = PageRequest.of(context.getFrom() / context.getSize(),
                context.getSize());

        State state = context.getState();

        List<Booking> result;
        LocalDateTime nowTime = LocalDateTime.now();
        switch (state) {
            case PAST:
                result = bookingRepository
                        .findByBookerIdAndEndBeforeOrderByStartDesc(bookerId,
                                nowTime,
                                pageable);
                break;
            case FUTURE:
                result = bookingRepository
                        .findByBookerIdAndStartAfterOrderByStartDesc(bookerId,
                                nowTime,
                                pageable);
                break;
            case CURRENT:
                result = bookingRepository
                        .findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(bookerId,
                                nowTime, nowTime,
                                pageable);
                break;
            case WAITING:
                result = bookingRepository
                        .findByBookerIdAndStatusOrderByStartDesc(bookerId,
                                BookingStatus.WAITING,
                                pageable);
                break;
            case REJECTED:
                result = bookingRepository
                        .findByBookerIdAndStatusOrderByStartDesc(bookerId,
                                BookingStatus.REJECTED,
                                pageable);
                break;
            case STARTED:
                result = bookingRepository
                        .findByBookerIdAndStatusAndStartBefore(bookerId,
                                BookingStatus.APPROVED,
                                nowTime,
                                pageable);
                break;
            case COMPLETED:
                result = bookingRepository
                        .findByBookerIdAndStatusAndEndBefore(bookerId,
                                BookingStatus.APPROVED,
                                nowTime,
                                pageable);
                break;
            default:
                result = bookingRepository
                        .findByBookerIdOrderByStartDesc(bookerId, pageable);
                break;
        }

        return toBookingExtraDtoMapper.map(result);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<BookingExtraDto> retrieveForItemsOwner(ForStateBookingContext context) {
        log.info("получение сведений о бронированиях для отдельного владельца вещей");

        Long itemOwnerId = context.getSharerUserId();
        context.setSharerUser(userService.retrieve(itemOwnerId));

        sharerUserValidator.validate(context);

        Pageable pageable = PageRequest.of(context.getFrom() / context.getSize(),
                context.getSize());

        State state = context.getState();

        List<Booking> result;
        LocalDateTime nowTime = LocalDateTime.now();
        switch (state) {
            case PAST:
                result = bookingRepository
                        .findByItemOwnerIdAndEndBeforeOrderByStartDesc(itemOwnerId,
                                nowTime,
                                pageable);
                break;
            case FUTURE:
                result = bookingRepository
                        .findByItemOwnerIdAndStartAfterOrderByStartDesc(itemOwnerId,
                                nowTime,
                                pageable);
                break;
            case CURRENT:
                result = bookingRepository
                        .findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(itemOwnerId,
                                nowTime,
                                nowTime,
                                pageable);
                break;
            case WAITING:
                result = bookingRepository
                        .findByItemOwnerIdAndStatusOrderByStartDesc(itemOwnerId,
                                BookingStatus.WAITING,
                                pageable);
                break;
            case REJECTED:
                result = bookingRepository
                        .findByItemOwnerIdAndStatusOrderByStartDesc(itemOwnerId,
                                BookingStatus.REJECTED,
                                pageable);
                break;
            case STARTED:
                result = bookingRepository
                        .findByItemOwnerIdAndStatusAndStartBefore(itemOwnerId,
                                BookingStatus.APPROVED,
                                nowTime,
                                pageable);
                break;
            case COMPLETED:
                result = bookingRepository
                        .findByItemOwnerIdAndStatusAndEndBefore(itemOwnerId,
                                BookingStatus.APPROVED,
                                nowTime,
                                pageable);
                break;
            default:
                result = bookingRepository
                        .findByItemOwnerIdOrderByStartDesc(itemOwnerId,
                                pageable);
                break;
        }

        return toBookingExtraDtoMapper.map(result);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public Map<TypesOfBookingConnectionToItem, Booking> retrieveForItem(Long itemId) {
        Map<TypesOfBookingConnectionToItem, Booking> result = new HashMap<>();
        LocalDateTime nowTime = LocalDateTime.now();
        List<Booking> bookings;

        bookings = bookingRepository.findByItemIdAndStatusAndStartBeforeOrderByEndDesc(itemId,
                BookingStatus.APPROVED,
                nowTime);

        if (bookings.size() > 0) {
            result.put(TypesOfBookingConnectionToItem.LAST, bookings.get(0));
        }

        bookings = bookingRepository.findFirstByItemIdAndStatusAndStartAfterOrderByStart(itemId,
                BookingStatus.APPROVED,
                nowTime);

        if (bookings.size() > 0) {
            result.put(TypesOfBookingConnectionToItem.NEXT, bookings.get(0));
        }

        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Map<Long, Map<TypesOfBookingConnectionToItem, Booking>> retrieveForItems(List<Long> ids) {
        Map<Long, Map<TypesOfBookingConnectionToItem, Booking>> result = new HashMap<>();
        LocalDateTime nowTime = LocalDateTime.now();

        proceedBookingsForItem(result,
                bookingRepository.findLastBookingsForItems(ids,
                        BookingStatus.APPROVED,
                        nowTime),
                TypesOfBookingConnectionToItem.LAST);

        proceedBookingsForItem(result,
                bookingRepository.findNextBookingsForItems(ids,
                        BookingStatus.APPROVED,
                        nowTime),
                TypesOfBookingConnectionToItem.NEXT);

        return result;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public BookingExtraDto retrieve(BasicBookingContext context) {
        log.info("Получение записи по идентификатору");

        context.setSharerUser(userService.retrieve(context.getSharerUserId()));

        sharerUserValidator.validate(context);

        context.setBooking(retrieve(context.getTargetBookingId()));

        relatedToBookedItemUserValidator.validate(context);

        return toBookingExtraDtoMapper.map(context.getBooking());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<BookingExtraDto> retrieve() {
        log.info("получение записей обо всех бронированиях");

        return toBookingExtraDtoMapper.map(bookingRepository.findAll());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public BookingExtraDto approve(ApproveBookingContext context) {
        log.info("подтверждение/отмена бронирования");

        context.setBooking(retrieve(context.getTargetBookingId()));

        ownerOfBookedItemValidator.validate(context);
        alreadyApprovedBookingValidator.validate(context);

        if (context.getIsApproved()) {
            context.getBooking().setStatus(BookingStatus.APPROVED);
        } else {
            context.getBooking().setStatus(BookingStatus.REJECTED);
        }

        return toBookingExtraDtoMapper.map(bookingRepository.save(context.getBooking()));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(BasicBookingContext context) {
        log.info("удаление записи");

        bookingRepository.deleteById(context.getTargetBookingId());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Booking retrieve(Long bookingId) {
        Optional<Booking> result = bookingRepository.findById(bookingId);

        result.orElseThrow(() -> new NotFoundException("запись о бронировании не найдена"));

        return result.get();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Booking> retrieveSuccessfulBookings(Long bookerId) {

        return bookingRepository
                .findByBookerIdAndStatusAndEndBefore(bookerId,
                        BookingStatus.APPROVED,
                        LocalDateTime.now());

    }

    @Transactional(propagation = Propagation.REQUIRED)
    private void proceedBookingsForItem(Map<Long, Map<TypesOfBookingConnectionToItem, Booking>> bookingByItemsId,
                                        List<Booking> bookings,
                                        TypesOfBookingConnectionToItem type) {
        if (bookings.size() > 0) {
            Long lastItemId = -1L;
            for (Booking booking : bookings) {
                Long itemId = booking.getItem().getId();

                if (!itemId.equals(lastItemId)) {
                    Map<TypesOfBookingConnectionToItem, Booking> bookingsForOneItem =
                            bookingByItemsId.getOrDefault(itemId, new HashMap<>());
                    bookingsForOneItem.put(type, booking);
                    bookingByItemsId.put(itemId, bookingsForOneItem);
                }
            }
        }
    }

}
