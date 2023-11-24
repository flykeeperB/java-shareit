package ru.practicum.shareit.booking.mapping.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingExtraDto;
import ru.practicum.shareit.booking.mapping.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookingMapperImpl implements BookingMapper {

    @Override
    public BookingDto mapToBookingDto(Booking source) {
        return mapToBookingDto(source, new BookingDto());
    }

    @Override
    public BookingDto mapToBookingDto(Booking source, BookingDto target) {
        if (source == null) {
            return null;
        }

        target.setId(source.getId());
        target.setStart(source.getStart());
        target.setEnd(source.getEnd());
        if (source.getBooker() != null) {
            target.setBookerId(source.getBooker().getId());
        }

        return target;
    }

    @Override
    public BookingExtraDto mapToBookingExtraDto(Booking source) {
        if (source == null) {
            return null;
        }

        BookingExtraDto target = new BookingExtraDto();

        mapToBookingDto(source, target);
        target.setStatus(source.getStatus());

        if (source.getItem() != null) {
            target.setItemId(source.getItem().getId());
        }

        return target;
    }

    @Override
    public List<BookingExtraDto> mapToBookingExtraDto(List<Booking> source) {
        return source
                .stream()
                .map(this::mapToBookingExtraDto)
                .collect(Collectors.toList());
    }

    @Override
    public Booking mapToBooking(BookingExtraDto source, User sharerUser, Item item) {
        if (source == null) {
            return null;
        }

        Booking target = new Booking();

        target.setStart(source.getStart());
        target.setEnd(source.getEnd());
        target.setId(source.getId());
        target.setBooker(sharerUser);
        target.setItem(item);
        target.setStatus(BookingStatus.WAITING);

        return target;
    }
}
