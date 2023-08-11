package ru.practicum.shareit.item.mapping;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.mapping.GenericBookingMapper;
import ru.practicum.shareit.item.dto.ItemExtraDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ItemExtraMapper {

    private final GenericBookingMapper bookingMapper;
    private final ItemMapper itemMapper;

    public ItemExtraDto toDto(Item source, ItemExtraDto target) {
        if (source == null) {
            return null;
        }

        itemMapper.toDto(source, target);
        target.setNextBooking(bookingMapper.toDto(source.getNextBooking()));
        target.setLastBooking(bookingMapper.toDto(source.getLastBooking()));

        return target;
    }

    public ItemExtraDto toDto(Item source) {
        return toDto(source, new ItemExtraDto());
    }

    public List<ItemExtraDto> toDto(List<Item> source) {
        return source.stream().map(this::toDto).collect(Collectors.toList());
    }
}
