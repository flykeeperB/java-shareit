package ru.practicum.shareit.booking.mapping.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.contexts.CreateBookingContext;
import ru.practicum.shareit.booking.mapping.ToBookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

@Component
public class ToBookingMapperImpl implements ToBookingMapper {
    @Override
    public Booking map(CreateBookingContext context) {
        if (context.getBookingExtraDto() == null) {
            return null;
        }

        Booking target = new Booking();

        target.setStart(context.getBookingExtraDto().getStart());
        target.setEnd(context.getBookingExtraDto().getEnd());
        target.setId(context.getBookingExtraDto().getId());
        target.setBooker(context.getSharerUser());
        target.setItem(context.getItem());
        target.setStatus(BookingStatus.WAITING);

        return target;
    }
}
