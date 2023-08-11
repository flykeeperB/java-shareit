package ru.practicum.shareit.booking.validators.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.requestsModels.CreateBookingRequest;
import ru.practicum.shareit.booking.validators.CorrectnessOfBookingDatesValidator;
import ru.practicum.shareit.exception.ValidationException;

import java.time.LocalDateTime;

@Component
public class CorrectnessOfBookingDatesValidatorImpl implements CorrectnessOfBookingDatesValidator {

    @Override
    public void validate(CreateBookingRequest request) {
        BookingDto bookingDto = request.getBookingDto();

        if (bookingDto.getStart().isAfter(bookingDto.getEnd())) {
            throw new ValidationException("неправильные даты начала и окончания бронирования " +
                    "- даты окончания больше даты бронирования");
        }

        if (bookingDto.getStart().equals(bookingDto.getEnd())) {
            throw new ValidationException("неправильные даты начала и окончания бронирования - даты не должны быть равны");
        }

        if (bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new ValidationException("неправильная дата начала бронирования - дата не должна быть раньше текущей");
        }
    }

}
