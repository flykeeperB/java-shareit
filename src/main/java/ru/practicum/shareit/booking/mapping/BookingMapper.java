package ru.practicum.shareit.booking.mapping;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.AbstractMapper;
import ru.practicum.shareit.Mapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapping.ItemMapper;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserMapper;

import java.util.Optional;

@Component
@AllArgsConstructor
public class BookingMapper
        extends AbstractMapper<Booking, BookingDto>
        implements Mapper<Booking, BookingDto> {

    private final UserMapper userMapper;
    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final GenericBookingMapper genericBookingMapper;

    @Override
    public BookingDto toDto(Booking source, BookingDto target) {
        if (source == null) {
            return null;
        }

        genericBookingMapper.toDto(source, target);

        target.setStatus(source.getStatus());
        target.setBooker(userMapper.toDto(source.getBooker()));
        target.setItem(itemMapper.toDto(source.getItem()));

        return target;
    }

    @Override
    public Booking fromDto(BookingDto source, Booking target) {
        if (source == null) {
            return null;
        }

        genericBookingMapper.fromDto(source, target);

        target.setStatus(source.getStatus());
        target.setBooker(userMapper.fromDto(source.getBooker()));
        target.setItem(itemMapper.fromDto(source.getItem()));

        if ((target.getItem() == null) && (source.getItemId() != null)) {
            target.setItem(itemService.retrieve(source.getItemId(),
                    Optional.empty()));
        }

        return target;
    }

    @Override
    public BookingDto toDto(Booking source) {
        return toDto(source, new BookingDto());
    }

    @Override
    public Booking fromDto(BookingDto source) {
        return fromDto(source, new Booking());
    }
}
