package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingExtraDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.contexts.*;
import ru.practicum.shareit.booking.service.ControllerBookingService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final ControllerBookingService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingExtraDto create(@Valid @RequestBody BookingExtraDto bookingExtraDto,
                                  @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("обработка запроса на создание записи о бронировании");

        CreateBookingContext context = CreateBookingContext.builder()
                .sharerUserId(userId)
                .bookingExtraDto(bookingExtraDto)
                .build();

        return service.create(context);
    }

    @GetMapping("/{bookingId}")
    public BookingExtraDto retrieve(@PathVariable Long bookingId,
                                    @RequestHeader(name = "X-Sharer-User-Id",
                                            required = false) Long userId) {
        log.info("обработка запроса на получение сведений о конкретном бронировании");

        BasicBookingContext context = BasicBookingContext.builder()
                .sharerUserId(userId)
                .targetBookingId(bookingId)
                .build();

        return service.retrieve(context);
    }

    @GetMapping
    public List<BookingExtraDto> retrieveForBooker(@RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                   @RequestParam(defaultValue = "15") @Min(0) Integer size,
                                                   @RequestParam(name = "state",
                                                           required = false,
                                                           defaultValue = "ALL") String state,
                                                   @RequestHeader(name = "X-Sharer-User-Id",
                                                           required = false) Long userId) {
        log.info("обработка запроса на получение сведений о бронированиях для отдельного пользователя");

        ForStateBookingContext context = ForStateBookingContext.builder()
                .sharerUserId(userId)
                .state(parseState(state.toUpperCase()))
                .from(from)
                .size(size)
                .build();

        return service.retrieveForBooker(context);
    }

    @GetMapping("/owner")
    public List<BookingExtraDto> retrieveForItemsOwner(@RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                       @RequestParam(defaultValue = "15") @Min(0) Integer size,
                                                       @RequestParam(name = "state",
                                                               required = false,
                                                               defaultValue = "ALL") String state,
                                                       @RequestHeader(name = "X-Sharer-User-Id",
                                                               required = false) Long userId) {
        log.info("обработка запроса на получение сведений о бронированиях вещей, принадлежащих отдельному пользователю");

        ForStateBookingContext context =
                ForStateBookingContext.builder()
                        .sharerUserId(userId)
                        .state(parseState(state.toUpperCase()))
                        .from(from)
                        .size(size)
                        .build();

        return service.retrieveForItemsOwner(context);
    }

    @PatchMapping("/{bookingId}")
    public BookingExtraDto approve(@PathVariable("bookingId") Long bookingId,
                                   @RequestParam("approved") Boolean isApproved,
                                   @RequestHeader(name = "X-Sharer-User-Id",
                                           required = false) Long userId) {
        log.info("обработка запроса на подтверждение (отмену) бронирования");

        ApproveBookingContext context = ApproveBookingContext.builder()
                .sharerUserId(userId)
                .targetBookingId(bookingId)
                .isApproved(isApproved)
                .build();

        return service.approve(context);
    }


    @DeleteMapping("/{bookingId}")
    public void delete(@PathVariable Long bookingId,
                       @RequestHeader(name = "X-Sharer-User-Id",
                               required = false) Long userId) {
        log.info("обработка запроса на удаление записи о бронировании");

        BasicBookingContext context = BasicBookingContext.builder()
                .sharerUserId(userId)
                .targetBookingId(bookingId)
                .build();

        service.delete(context);
    }

    private State parseState(String state) {
        try {
            return State.valueOf(state);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unknown state: " + state);
        }
    }
}
