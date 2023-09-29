package ru.practicum.shareit.booking.validators.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingExtraDto;
import ru.practicum.shareit.booking.validators.CorrectnessOfBookingDatesValidator;
import ru.practicum.shareit.exception.ValidationException;

import java.time.LocalDateTime;

@Component
public class CorrectnessOfBookingDatesValidatorImpl implements CorrectnessOfBookingDatesValidator {

    @Override
    public void validate(BookingExtraDto bookingExtraDto) {

        if (bookingExtraDto.getStart() == null) {
            throw new ValidationException("дата начала бронирования не определена");
        }

        if (bookingExtraDto.getEnd() == null) {
            throw new ValidationException("дата оконания бронирования не определена");
        }

        if (bookingExtraDto.getStart().isAfter(bookingExtraDto.getEnd())) {
            throw new ValidationException("неправильные даты начала и окончания бронирования " +
                    "- даты окончания больше даты бронирования");
        }

        if (bookingExtraDto.getStart().equals(bookingExtraDto.getEnd())) {
            throw new ValidationException("неправильные даты начала и окончания бронирования - даты не должны быть равны");
        }

        if (bookingExtraDto.getStart().isBefore(LocalDateTime.now())) {
            throw new ValidationException("неправильная дата начала бронирования - дата не должна быть раньше текущей");
        }
    }

}
