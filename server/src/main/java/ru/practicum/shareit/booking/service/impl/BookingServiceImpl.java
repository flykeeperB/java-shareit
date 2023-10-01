package ru.practicum.shareit.booking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.mapping.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingExtraDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.contexts.*;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.validators.*;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.mapping.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.mapping.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    private final BookingMapper bookingMapper;
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;

    private final AvailabilityForBookingValidator availabilityForBookingValidator;
    private final RelatedToBookedItemUserValidator relatedToBookedItemUserValidator;
    private final OwnerOfBookedItemValidator ownerOfBookedItemValidator;
    private final AlreadyApprovedBookingValidator alreadyApprovedBookingValidator;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public BookingExtraDto create(CreateBookingContext context) {
        log.info("создание записи");

        User sharerUser = retrieveUser(context.getSharerUserId());

        Item item = retrieveItem(context.getBookingExtraDto().getItemId());

        availabilityForBookingValidator.validate(context.getSharerUserId(), item);

        context.getBookingExtraDto().setBookerId(context.getSharerUserId());

        Booking booking = bookingMapper.mapToBooking(context.getBookingExtraDto(), sharerUser, item);

        Booking newBooking = bookingRepository.save(booking);

        BookingExtraDto result = bookingMapper.mapToBookingExtraDto(newBooking);

        result.setBooker(userMapper.mapToUserDto(newBooking.getBooker()));
        result.setItem(itemMapper.mapToItemDto(newBooking.getItem()));

        return result;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<BookingExtraDto> retrieveForBooker(ForStateBookingContext context) {
        log.info("получение сведений о бронированиях для отдельного пользователя");

        retrieveUser(context.getSharerUserId()); //выбросит исключение, если пользователь не найден

        Long bookerId = context.getSharerUserId();

        Pageable pageable = PageRequest.of(context.getFrom() / context.getSize(),
                context.getSize());

        State state = context.getState();

        List<Booking> bookings;
        LocalDateTime nowTime = LocalDateTime.now();
        switch (state) {
            case PAST:
                bookings = bookingRepository
                        .findByBookerIdAndEndBeforeOrderByStartDesc(bookerId,
                                nowTime,
                                pageable);
                break;
            case FUTURE:
                bookings = bookingRepository
                        .findByBookerIdAndStartAfterOrderByStartDesc(bookerId,
                                nowTime,
                                pageable);
                break;
            case CURRENT:
                bookings = bookingRepository
                        .findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(bookerId,
                                nowTime, nowTime,
                                pageable);
                break;
            case WAITING:
                bookings = bookingRepository
                        .findByBookerIdAndStatusOrderByStartDesc(bookerId,
                                BookingStatus.WAITING,
                                pageable);
                break;
            case REJECTED:
                bookings = bookingRepository
                        .findByBookerIdAndStatusOrderByStartDesc(bookerId,
                                BookingStatus.REJECTED,
                                pageable);
                break;
            case STARTED:
                bookings = bookingRepository
                        .findByBookerIdAndStatusAndStartBefore(bookerId,
                                BookingStatus.APPROVED,
                                nowTime,
                                pageable);
                break;
            case COMPLETED:
                bookings = bookingRepository
                        .findByBookerIdAndStatusAndEndBefore(bookerId,
                                BookingStatus.APPROVED,
                                nowTime,
                                pageable);
                break;
            default:
                bookings = bookingRepository
                        .findByBookerIdOrderByStartDesc(bookerId, pageable);
                break;
        }

        List<BookingExtraDto> result = new ArrayList<>();
        for (Booking booking : bookings) {
            BookingExtraDto bookingExtraDto = bookingMapper.mapToBookingExtraDto(booking);
            bookingExtraDto.setBooker(userMapper.mapToUserDto(booking.getBooker()));
            bookingExtraDto.setItem(itemMapper.mapToItemDto(booking.getItem()));
            result.add(bookingExtraDto);
        }

        return result;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<BookingExtraDto> retrieveForItemsOwner(ForStateBookingContext context) {
        log.info("получение сведений о бронированиях для отдельного владельца вещей");

        retrieveUser(context.getSharerUserId()); // выбросит исключение, если пользователь не будет найден

        Long itemOwnerId = context.getSharerUserId();

        Pageable pageable = PageRequest.of(context.getFrom() / context.getSize(),
                context.getSize());

        State state = context.getState();

        List<Booking> bookings;
        LocalDateTime nowTime = LocalDateTime.now();
        switch (state) {
            case PAST:
                bookings = bookingRepository
                        .findByItemOwnerIdAndEndBeforeOrderByStartDesc(itemOwnerId,
                                nowTime,
                                pageable);
                break;
            case FUTURE:
                bookings = bookingRepository
                        .findByItemOwnerIdAndStartAfterOrderByStartDesc(itemOwnerId,
                                nowTime,
                                pageable);
                break;
            case CURRENT:
                bookings = bookingRepository
                        .findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(itemOwnerId,
                                nowTime,
                                nowTime,
                                pageable);
                break;
            case WAITING:
                bookings = bookingRepository
                        .findByItemOwnerIdAndStatusOrderByStartDesc(itemOwnerId,
                                BookingStatus.WAITING,
                                pageable);
                break;
            case REJECTED:
                bookings = bookingRepository
                        .findByItemOwnerIdAndStatusOrderByStartDesc(itemOwnerId,
                                BookingStatus.REJECTED,
                                pageable);
                break;
            case STARTED:
                bookings = bookingRepository
                        .findByItemOwnerIdAndStatusAndStartBefore(itemOwnerId,
                                BookingStatus.APPROVED,
                                nowTime,
                                pageable);
                break;
            case COMPLETED:
                bookings = bookingRepository
                        .findByItemOwnerIdAndStatusAndEndBefore(itemOwnerId,
                                BookingStatus.APPROVED,
                                nowTime,
                                pageable);
                break;
            default:
                bookings = bookingRepository
                        .findByItemOwnerIdOrderByStartDesc(itemOwnerId,
                                pageable);
                break;
        }

        List<BookingExtraDto> result = new ArrayList<>();
        for (Booking booking : bookings) {
            BookingExtraDto bookingExtraDto = bookingMapper.mapToBookingExtraDto(booking);
            bookingExtraDto.setBooker(userMapper.mapToUserDto(booking.getBooker()));
            bookingExtraDto.setItem(itemMapper.mapToItemDto(booking.getItem()));
            result.add(bookingExtraDto);
        }

        return result;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public BookingExtraDto retrieve(BasicBookingContext context) {
        log.info("Получение записи по идентификатору");

        retrieveUser(context.getSharerUserId()); //выбросит исключение, если пользователь не нейден

        Booking booking = retrieve(context.getTargetBookingId());

        relatedToBookedItemUserValidator.validate(context.getSharerUserId(), booking);

        BookingExtraDto result = bookingMapper.mapToBookingExtraDto(booking);

        result.setBooker(userMapper.mapToUserDto(booking.getBooker()));
        result.setItem(itemMapper.mapToItemDto(booking.getItem()));

        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<BookingExtraDto> retrieve() {
        log.info("получение записей обо всех бронированиях");

        return bookingMapper.mapToBookingExtraDto(bookingRepository.findAll());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public BookingExtraDto approve(ApproveBookingContext context) {
        log.info("подтверждение/отмена бронирования ");

        Booking booking = retrieve(context.getTargetBookingId());

        ownerOfBookedItemValidator.validate(context.getSharerUserId(), booking);

        if (context.getIsApproved()) {
            alreadyApprovedBookingValidator.validate(booking);
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }

        booking = bookingRepository.save(booking);

        BookingExtraDto result = bookingMapper.mapToBookingExtraDto(booking);

        result.setBooker(userMapper.mapToUserDto(booking.getBooker()));
        result.setItem(itemMapper.mapToItemDto(booking.getItem()));

        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(BasicBookingContext context) {
        log.info("удаление записи");

        bookingRepository.deleteById(context.getTargetBookingId());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    private Booking retrieve(Long bookingId) {
        Optional<Booking> result = bookingRepository.findById(bookingId);

        result.orElseThrow(() -> new NotFoundException("запись о бронировании не найдена"));

        return result.get();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    private Item retrieveItem(Long itemId) {
        Optional<Item> result = itemRepository.findById(itemId);

        result.orElseThrow(() -> new NotFoundException("запись не найдена"));

        return result.get();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    private User retrieveUser(Long id) {
        Optional<User> result = userRepository.findById(id);

        result.orElseThrow(() -> new NotFoundException("запись о пользователе не найдена"));

        return result.get();
    }

}
