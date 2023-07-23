package ru.practicum.shareit.item.mapping;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.AbstractMapper;
import ru.practicum.shareit.Mapper;
import ru.practicum.shareit.booking.mapping.GenericBookingMapper;
import ru.practicum.shareit.item.dto.ItemExtraDto;
import ru.practicum.shareit.item.model.Item;

@Component
@AllArgsConstructor
public class ItemExtraMapper
        extends AbstractMapper<Item, ItemExtraDto>
        implements Mapper<Item, ItemExtraDto> {

    private final ItemMapper itemMapper;
    private final GenericBookingMapper bookingMapper;

    @Override
    public ItemExtraDto toDto(Item source, ItemExtraDto target) {
        if (source == null) {
            return null;
        }

        itemMapper.toDto(source, target);
        target.setNextBooking(bookingMapper.toDto(source.getNextBooking()));
        target.setLastBooking(bookingMapper.toDto(source.getLastBooking()));

        return target;
    }

    @Override
    public Item fromDto(ItemExtraDto source, Item target) {
        if (source == null) {
            return null;
        }

        itemMapper.fromDto(source, target);

        return target;
    }

    @Override
    public ItemExtraDto toDto(Item source) {
        return toDto(source, new ItemExtraDto());
    }

    @Override
    public Item fromDto(ItemExtraDto source) {
        return fromDto(source, new Item());
    }
}
