package ru.practicum.shareit.booking.mapping;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.GenericBookingDto;
import ru.practicum.shareit.booking.model.Booking;

@Component
@AllArgsConstructor
public class GenericBookingMapper {

    public GenericBookingDto toDto(Booking source, GenericBookingDto target) {
        if (source == null) {
            return null;
        }

        target.setStart(source.getStart());
        target.setEnd(source.getEnd());
        if (source.getBooker() != null) {
            target.setBookerId(source.getBooker().getId());
        }

        target.setId(source.getId());

        return target;
    }

    public GenericBookingDto toDto(Booking source) {
        return toDto(source, new GenericBookingDto());
    }

    public Booking fromDto(GenericBookingDto source, Booking target) {
        if (source == null) {
            return null;
        }

        target.setStart(source.getStart());
        target.setEnd(source.getEnd());

        target.setId(source.getId());

        return target;
    }
}
