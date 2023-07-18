package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.AbstractMapper;
import ru.practicum.shareit.Mapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.user.UserMapper;

@Component
@AllArgsConstructor
public class ItemMapper
        extends AbstractMapper<Item, ItemDto>
        implements Mapper<Item, ItemDto> {

    UserMapper userMapper;
    ItemRequestMapper itemRequestMapper;

    @Override
    public ItemDto toDto(Item source) {
        if (source == null) {
            return null;
        }

        ItemDto result = ItemDto.builder()
                .name(source.getName())
                .description(source.getDescription())
                .available(source.getAvailable())
                .owner(userMapper.toDto(source.getOwner()))
                .itemRequestDto(itemRequestMapper.toDto(source.getItemRequest()))
                .build();

        result.setId(source.getId());

        return result;
    }

    @Override
    public Item fromDto(ItemDto source) {
        if (source == null) {
            return null;
        }

        Item result = Item.builder()
                .name(source.getName())
                .description(source.getDescription())
                .available(source.getAvailable())
                .owner(userMapper.fromDto(source.getOwner()))
                .itemRequest(itemRequestMapper.fromDto(source.getItemRequestDto()))
                .build();

        result.setId(source.getId());

        return result;
    }
}
