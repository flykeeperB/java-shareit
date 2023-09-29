package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@PostMapping
	public ResponseEntity<Object> create(@Valid @RequestBody BookingExtraDto bookingDto,
								  		 @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
		log.info("обработка запроса на создание записи о бронировании");

		return bookingClient.addNew(userId, bookingDto);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> retrieve(@PathVariable Long bookingId,
										   @RequestHeader(name = "X-Sharer-User-Id",
											required = false) Long userId) {
		log.info("обработка запроса на получение сведений о конкретном бронировании");

		return bookingClient.getBooking(userId, bookingId);
	}

	@GetMapping
	public ResponseEntity<Object> retrieveForBooker(@RequestParam(defaultValue = "0") @Min(0) Integer from,
												   @RequestParam(defaultValue = "15") @Min(1) Integer size,
												   @RequestParam(name = "state",
														   required = false,
														   defaultValue = "ALL") String state,
												   @RequestHeader(name = "X-Sharer-User-Id",
														   required = false) Long userId) {
		log.info("обработка запроса на получение сведений о бронированиях для отдельного пользователя");

		return bookingClient.getBookings(userId, parseState(state), from, size);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> retrieveForItemsOwner(@RequestParam(defaultValue = "0") @Min(0) Integer from,
													   @RequestParam(defaultValue = "15") @Min(1) Integer size,
													   @RequestParam(name = "state",
															   required = false,
															   defaultValue = "ALL") String state,
													   @RequestHeader(name = "X-Sharer-User-Id",
															   required = false) Long userId) {
		log.info("обработка запроса на получение сведений о бронированиях вещей, принадлежащих отдельному пользователю");

		return bookingClient.getItemsBookings(userId, parseState(state), from, size);
	}

	@PatchMapping("/{bookingId}")
	public ResponseEntity<Object>approve(@PathVariable("bookingId") Long bookingId,
								   @RequestParam("approved") String approved,
								   @RequestHeader(name = "X-Sharer-User-Id",
										   required = false) Long userId) {
		log.info("обработка запроса на подтверждение (отмену) бронирования");

		return bookingClient.approve(userId, bookingId, approved);
	}

	private BookingState parseState(String state) {
		try {
			return BookingState.valueOf(state);
		} catch (Exception e) {
			throw new IllegalArgumentException("Unknown state: " + state);
		}
	}

}
