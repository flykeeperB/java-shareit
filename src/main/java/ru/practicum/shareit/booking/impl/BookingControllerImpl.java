package ru.practicum.shareit.booking.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.AbstractController;
import ru.practicum.shareit.booking.mapping.BookingMapper;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.model.Booking;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/bookings")
public class BookingControllerImpl extends AbstractController<BookingDto, Booking> {

    private final BookingService service;
    private final BookingMapper mapper;

    @Autowired
    public BookingControllerImpl(BookingService service,
                                 BookingMapper mapper) {
        super(service, mapper);
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    @Override
    public BookingDto create(@Valid @RequestBody BookingDto source,
                             @RequestHeader("X-Sharer-User-Id") Optional<Long> userId) {
        logInfo("обработка запроса на создание записи о бронировании");

        return super.create(source, userId);
    }

    @GetMapping("/{bookingId}")
    @Override
    public BookingDto retrieve(@PathVariable Long bookingId,
                               @RequestHeader("X-Sharer-User-Id") Optional<Long> userId) {
        logInfo("обработка запроса на получение сведений о конкретном бронировании");

        return super.retrieve(bookingId, userId);
    }

    @Override
    public List<BookingDto> retrieve(@RequestHeader("X-Sharer-User-Id") Optional<Long> userId) {
        return super.retrieve(userId);
    }

    private State parseState(String state) {
        try {
            return State.valueOf(state);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unknown state: " + state);
        }
    }

    @GetMapping
    public List<BookingDto> retrieve(@RequestParam(name = "state",
            required = false,
            defaultValue = "ALL") String state,
                                     @RequestHeader("X-Sharer-User-Id") Optional<Long> userId) {
        logInfo("обработка запроса на получение сведений о бронированиях для отдельного пользователя");

        return mapper.toDto(service.retrieveForBooker(userId,
                parseState(state.toUpperCase())));

    }

    @GetMapping("/owner")
    public List<BookingDto> retrieveForItemsOwner(@RequestParam(name = "state",
            required = false,
            defaultValue = "ALL") String state,
                                                  @RequestHeader("X-Sharer-User-Id") Optional<Long> userId) {
        logInfo("обработка запроса на получение сведений о бронированиях вещей, принадлежащих отдельному пользователю");

        return mapper.toDto(service.retrieveForItemsOwner(userId, parseState(state)));

    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(@PathVariable("bookingId") Long id,
                              @RequestParam("approved") Boolean isApproved,
                              @RequestHeader("X-Sharer-User-Id") Optional<Long> userId) {
        logInfo("обработка запроса на подтверждение (отмену) бронирования");

        return mapper.toDto(service.approve(id, userId, isApproved));
    }


    @DeleteMapping("/{itemId}")
    @Override
    public void delete(@PathVariable Long itemId,
                       @RequestHeader("X-Sharer-User-Id") Optional<Long> userId) {
        logInfo("обработка запроса на удаление записи о бронировании");

        super.delete(itemId, userId);
    }

    @Override
    public String getName() {
        return "Booking";
    }
}
