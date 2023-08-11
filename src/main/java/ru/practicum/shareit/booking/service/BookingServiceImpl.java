package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.mapping.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.requestsModels.*;
import ru.practicum.shareit.booking.validators.*;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    private final AvailabilityForBookingValidator availabilityForBookingValidator;
    private final CorrectnessOfBookingDatesValidator correctnessOfBookingDatesValidator;
    private final SharerUserValidator sharerUserValidator;
    private final RelatedToBookedItemUserValidator relatedToBookedItemUserValidator;
    private final OwnerOfBookedItemValidator ownerOfBookedItemValidator;
    private final AlreadyApprovedBookingValidator alreadyApprovedBookingValidator;

    @Lazy
    private final UserService userService;
    @Lazy
    private final ItemService itemService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public BookingDto create(CreateBookingRequest request) {
        log.info("создание записи");

        availabilityForBookingValidator.validate(request);
        correctnessOfBookingDatesValidator.validate(request);
        sharerUserValidator.validate(request);

        request.getBookingDto().setBookerId(request.getSharerUserId());

        Booking booking = bookingMapper.fromDto(request.getBookingDto());

        User user = userService.retrieve(request.getSharerUserId());
        booking.setBooker(user);

        Item item = itemService.retrieve(request.getBookingDto().getItemId());
        booking.setItem(item);

        booking.setStatus(BookingStatus.WAITING);

        Booking result = bookingRepository.save(booking);

        return bookingMapper.toDto(result);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<BookingDto> retrieveForBooker(ForStateBookingRequest request) {
        log.info("получение сведений о бронированиях для отдельного пользователя");

        sharerUserValidator.validate(request);

        Long ownerId = request.getSharerUserId();

        State state = request.getState();

        List<Booking> result;
        LocalDateTime nowTime = LocalDateTime.now();
        switch (state) {
            case PAST:
                result = bookingRepository
                        .findByBookerIdAndEndBeforeOrderByStartDesc(ownerId, nowTime);
                break;
            case FUTURE:
                result = bookingRepository
                        .findByBookerIdAndStartAfterOrderByStartDesc(ownerId, nowTime);
                break;
            case CURRENT:
                result = bookingRepository
                        .findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(ownerId, nowTime, nowTime);
                break;
            case WAITING:
                result = bookingRepository
                        .findByBookerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.WAITING);
                break;
            case REJECTED:
                result = bookingRepository
                        .findByBookerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.REJECTED);
                break;
            case STARTED:
                result = bookingRepository
                        .findByBookerIdAndStatusAndStartBefore(ownerId, BookingStatus.APPROVED, nowTime);
                break;
            case COMPLETED:
                result = bookingRepository
                        .findByBookerIdAndStatusAndEndBefore(ownerId, BookingStatus.APPROVED, nowTime);
                break;
            default:
                result = bookingRepository
                        .findByBookerIdOrderByStartDesc(ownerId);
                break;
        }

        return bookingMapper.toDto(result);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<BookingDto> retrieveForItemsOwner(ForStateBookingRequest request) {
        log.info("получение сведений о бронированиях для отдельного владельца вещей");

        sharerUserValidator.validate(request);

        Long itemOwnerId = request.getSharerUserId();

        State state = request.getState();

        List<Booking> result;
        LocalDateTime nowTime = LocalDateTime.now();
        switch (state) {
            case PAST:
                result = bookingRepository
                        .findByItemOwnerIdAndEndBeforeOrderByStartDesc(itemOwnerId, nowTime);
                break;
            case FUTURE:
                result = bookingRepository
                        .findByItemOwnerIdAndStartAfterOrderByStartDesc(itemOwnerId, nowTime);
                break;
            case CURRENT:
                result = bookingRepository
                        .findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(itemOwnerId, nowTime, nowTime);
                break;
            case WAITING:
                result = bookingRepository
                        .findByItemOwnerIdAndStatusOrderByStartDesc(itemOwnerId, BookingStatus.WAITING);
                break;
            case REJECTED:
                result = bookingRepository
                        .findByItemOwnerIdAndStatusOrderByStartDesc(itemOwnerId, BookingStatus.REJECTED);
                break;
            case STARTED:
                result = bookingRepository
                        .findByItemOwnerIdAndStatusAndStartBefore(itemOwnerId, BookingStatus.APPROVED, nowTime);
                break;
            case COMPLETED:
                result = bookingRepository
                        .findByItemOwnerIdAndStatusAndEndBefore(itemOwnerId, BookingStatus.APPROVED, nowTime);
                break;
            default:
                result = bookingRepository
                        .findByItemOwnerIdOrderByStartDesc(itemOwnerId);
                break;
        }

        return bookingMapper.toDto(result);
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
    public BookingDto retrieve(BasicBookingRequest basicBookingRequest) {
        log.info("Получение записи по идентификатору");

        sharerUserValidator.validate(basicBookingRequest);

        Booking booking = retrieve(basicBookingRequest.getTargetBookingId());

        relatedToBookedItemUserValidator.validate(basicBookingRequest, booking);

        return bookingMapper.toDto(booking);
    }

    @Override
    public List<BookingDto> retrieve() {
        log.info("получение записей обо всех бронированиях");

        return bookingMapper.toDto(bookingRepository.findAll());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public BookingDto approve(ApproveBookingRequest approveBookingRequest) {
        log.info("подтверждение/отмена бронирования");

        Booking booking = this.retrieve(approveBookingRequest.getTargetBookingId());

        ownerOfBookedItemValidator.validate(approveBookingRequest, booking);
        alreadyApprovedBookingValidator.validate(approveBookingRequest, booking);

        if (approveBookingRequest.getIsApproved()) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    @Override
    public void delete(BasicBookingRequest request) {
        log.info("удаление записи");

        bookingRepository.deleteById(request.getTargetBookingId());
    }

    @Override
    public Booking retrieve(Long bookingId) {
        Optional<Booking> result = bookingRepository.findById(bookingId);

        result.orElseThrow(() -> new NotFoundException("запись о бронировании не найдена"));

        return result.get();
    }

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
