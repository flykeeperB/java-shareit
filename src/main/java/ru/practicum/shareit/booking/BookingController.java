package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.requestsModels.*;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService service;

    @PostMapping
    public BookingDto create(@Valid @RequestBody BookingDto bookingDto,
                             @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("обработка запроса на создание записи о бронировании");

        CreateBookingRequest createBookingRequest = CreateBookingRequest.builder()
                .sharerUserId(userId)
                .bookingDto(bookingDto)
                .build();

        return service.create(createBookingRequest);
    }

    @GetMapping("/{bookingId}")
    public BookingDto retrieve(@PathVariable Long bookingId,
                               @RequestHeader(name = "X-Sharer-User-Id",
                                       required = false) Long userId) {
        log.info("обработка запроса на получение сведений о конкретном бронировании");

        BasicBookingRequest basicBookingRequest = BasicBookingRequest.builder()
                .sharerUserId(userId)
                .targetBookingId(bookingId)
                .build();

        return service.retrieve(basicBookingRequest);
    }

    @GetMapping
    public List<BookingDto> retrieveForBooker(@RequestParam(name = "state",
            required = false,
            defaultValue = "ALL") String state,
                                              @RequestHeader(name = "X-Sharer-User-Id",
                                                      required = false) Long userId) {
        log.info("обработка запроса на получение сведений о бронированиях для отдельного пользователя");

        ForStateBookingRequest forStateBookingRequest = ForStateBookingRequest.builder()
                .sharerUserId(userId)
                .state(parseState(state.toUpperCase()))
                .build();

        return service.retrieveForBooker(forStateBookingRequest);
    }

    @GetMapping("/owner")
    public List<BookingDto> retrieveForItemsOwner(@RequestParam(name = "state",
            required = false,
            defaultValue = "ALL") String state,
                                                  @RequestHeader(name = "X-Sharer-User-Id",
                                                          required = false) Long userId) {
        log.info("обработка запроса на получение сведений о бронированиях вещей, принадлежащих отдельному пользователю");

        ForStateBookingRequest request =
                ForStateBookingRequest.builder()
                        .sharerUserId(userId)
                        .state(parseState(state.toUpperCase()))
                        .build();

        return service.retrieveForItemsOwner(request);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(@PathVariable("bookingId") Long bookingId,
                              @RequestParam("approved") Boolean isApproved,
                              @RequestHeader(name = "X-Sharer-User-Id",
                                      required = false) Long userId) {
        log.info("обработка запроса на подтверждение (отмену) бронирования");

        ApproveBookingRequest approveBookingRequest = ApproveBookingRequest.builder()
                .sharerUserId(userId)
                .targetBookingId(bookingId)
                .isApproved(isApproved)
                .build();

        return service.approve(approveBookingRequest);
    }


    @DeleteMapping("/{bookingId}")
    public void delete(@PathVariable Long bookingId,
                       @RequestHeader(name = "X-Sharer-User-Id",
                               required = false) Long userId) {
        log.info("обработка запроса на удаление записи о бронировании");

        BasicBookingRequest request = BasicBookingRequest.builder()
                .sharerUserId(userId)
                .targetBookingId(bookingId)
                .build();

        service.delete(request);
    }

    private State parseState(String state) {
        try {
            return State.valueOf(state);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unknown state: " + state);
        }
    }
}
