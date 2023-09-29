package ru.practicum.shareit.booking.validators.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.validators.CorrectnessOfBookingDatesValidator;
import ru.practicum.shareit.exception.ValidationException;

import java.time.LocalDateTime;

@Component
public class CorrectnessOfBookingDatesValidatorImpl implements CorrectnessOfBookingDatesValidator {

    @Override
    public void validate(BookingDto bookingDto) {

        if (bookingDto.getStart() == null) {
            throw new ValidationException("дата начала бронирования не определена");
        }

        if (bookingDto.getEnd() == null) {
            throw new ValidationException("дата оконания бронирования не определена");
        }

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
