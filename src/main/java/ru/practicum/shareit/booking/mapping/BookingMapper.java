package ru.practicum.shareit.booking.mapping;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapping.ItemMapper;
import ru.practicum.shareit.user.mapping.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class BookingMapper {

    private final UserMapper userMapper;
    private final ItemMapper itemMapper;
    private final GenericBookingMapper genericBookingMapper;

    public BookingDto toDto(Booking source, BookingDto target) {
        if (source == null) {
            return null;
        }

        genericBookingMapper.toDto(source, target);

        target.setStatus(source.getStatus());
        target.setBooker(userMapper.toDto(source.getBooker()));
        target.setItem(itemMapper.toDto(source.getItem()));

        if (source.getItem() != null) {
            target.setItemId(source.getItem().getId());
        }

        return target;
    }

    public Booking fromDto(BookingDto source, Booking target) {
        if (source == null) {
            return null;
        }

        genericBookingMapper.fromDto(source, target);

        target.setStatus(source.getStatus());

        return target;
    }

    public BookingDto toDto(Booking source) {
        return toDto(source, new BookingDto());
    }

    public Booking fromDto(BookingDto source) {
        return fromDto(source, new Booking());
    }

    public List<BookingDto> toDto(List<Booking> source) {
        return source
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
