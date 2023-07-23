package ru.practicum.shareit.booking.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.AbstractService;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingValidator;
import ru.practicum.shareit.booking.TypesOfBookingConnectionToItem;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class BookingServiceImpl
        extends AbstractService<Booking, BookingValidator>
        implements BookingService {

    private final BookingRepository repository;
    private final UserService userService;
    //private final ItemService itemService;

    @Autowired
    @Lazy
    public BookingServiceImpl(BookingRepository repository,
                              BookingValidator validator,
                              UserService userService,
                              ItemService itemService) {
        super(repository, validator);
        this.repository = repository;
        this.userService = userService;
        //this.itemService = itemService;
    }

    @Override
    public Booking create(Booking source, Optional<Long> userId) {
        this.validator.validateUserId(userId);

        source.setBooker(userService.retrieve(userId.get(), userId));
        source.setStatus(BookingStatus.WAITING);

        return super.create(source, userId);
    }

    public List<Booking> retrieveForBooker(Optional<Long> userId, State state) {
        logInfo("получение сведений о бронированиях для отдельного пользователя");

        validator.validateUserId(userId);

        // при поиске пользователя также выполняется валидация его наличия
        userService.retrieve(userId.get(), userId);

        Long id = userId.get();

        List<Booking> result;
        LocalDateTime nowTime = LocalDateTime.now();
        switch (state) {
            case PAST:
                result = repository.findByBookerIdAndEndBeforeOrderByStartDesc(id, nowTime);
                break;
            case FUTURE:
                result = repository.findByBookerIdAndStartAfterOrderByStartDesc(id, nowTime);
                break;
            case CURRENT:
                result = repository.findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(id, nowTime, nowTime);
                break;
            case WAITING:
                result = repository.findByBookerIdAndStatusOrderByStartDesc(id, BookingStatus.WAITING);
                break;
            case REJECTED:
                result = repository.findByBookerIdAndStatusOrderByStartDesc(id, BookingStatus.REJECTED);
                break;
            case STARTED:
                result = repository.findByBookerIdAndStatusAndStartBefore(id, BookingStatus.APPROVED, nowTime);
                break;
            case COMPLETED:
                result = repository.findByBookerIdAndStatusAndEndBefore(id, BookingStatus.APPROVED, nowTime);
                break;
            default:
                result = repository.findByBookerIdOrderByStartDesc(id);
                break;
        }

        return result;
    }

    @Override
    public List<Booking> retrieveForItemsOwner(Optional<Long> userId, State state) {
        logInfo("получение сведений о бронированиях для отдельного владельца вещей");

        validator.validateUserId(userId);

        // при поиске пользователя также выполняется валидация его наличия
        userService.retrieve(userId.get(), userId);

        Long id = userId.get();

        List<Booking> result;
        LocalDateTime nowTime = LocalDateTime.now();
        switch (state) {
            case PAST:
                result = repository.findByItemOwnerIdAndEndBeforeOrderByStartDesc(id, nowTime);
                break;
            case FUTURE:
                result = repository.findByItemOwnerIdAndStartAfterOrderByStartDesc(id, nowTime);
                break;
            case CURRENT:
                result = repository.findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(id, nowTime, nowTime);
                break;
            case WAITING:
                result = repository.findByItemOwnerIdAndStatusOrderByStartDesc(id, BookingStatus.WAITING);
                break;
            case REJECTED:
                result = repository.findByItemOwnerIdAndStatusOrderByStartDesc(id, BookingStatus.REJECTED);
                break;
            case STARTED:
                result = repository.findByItemOwnerIdAndStatusAndStartBefore(id, BookingStatus.APPROVED, nowTime);
                break;
            case COMPLETED:
                result = repository.findByItemOwnerIdAndStatusAndEndBefore(id, BookingStatus.APPROVED, nowTime);
                break;
            default:
                result = repository.findByItemOwnerIdOrderByStartDesc(id);
                break;
        }

        return result;
    }

    @Override
    public Map<TypesOfBookingConnectionToItem, Booking> retrieveForItem(Long itemId) {
        Map<TypesOfBookingConnectionToItem, Booking> result = new HashMap<>();
        LocalDateTime nowTime = LocalDateTime.now();
        List<Booking> bookings;

        bookings = repository.findByItemIdAndStatusAndStartBeforeOrderByEndDesc(itemId,
                BookingStatus.APPROVED,
                nowTime);

        if (bookings.size() > 0) {
            result.put(TypesOfBookingConnectionToItem.LAST, bookings.get(0));
        }

        bookings = repository.findFirstByItemIdAndStatusAndStartAfterOrderByStart(itemId,
                BookingStatus.APPROVED,
                nowTime);

        if (bookings.size() > 0) {
            result.put(TypesOfBookingConnectionToItem.NEXT, bookings.get(0));
        }

        return result;
    }

    @Override
    public Booking update(Booking source, Optional<Long> userId) {
        return super.update(source, userId);
    }

    @Override
    public void delete(Long id, Optional<Long> userId) {
        super.delete(id, userId);
    }

    @Override
    public Booking retrieve(Long id, Optional<Long> userId) {
        Booking result = super.retrieve(id, userId);

        validator.forRetrieve(result, userId);

        return result;
    }

    @Override
    public Booking approve(Long id, Optional<Long> userId, Boolean isApproved) {
        logInfo("подтверждение/отмена бронирования");

        Booking target = super.retrieve(id, userId);

        validator.forApprove(target, userId, isApproved);

        if (isApproved) {
            target.setStatus(BookingStatus.APPROVED);
        } else {
            target.setStatus(BookingStatus.REJECTED);
        }

        return repository.save(target);
    }

    @Override
    public String getName() {
        return "Booking";
    }

    @Override
    public List<Booking> retrieveComplitedForBooker(Optional<Long> userId, State state) {
        return null;
    }
}
