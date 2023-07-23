package ru.practicum.shareit.item.mapping;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.AbstractMapper;
import ru.practicum.shareit.Mapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.user.UserMapper;

import java.util.ArrayList;

@Component
@AllArgsConstructor
public class ItemMapper
        extends AbstractMapper<Item, ItemDto>
        implements Mapper<Item, ItemDto> {

    private final UserMapper userMapper;
    // private final ItemRequestMapper itemRequestMapper;
    private final CommentMapper commentMapper;

    @Override
    public ItemDto toDto(Item source, ItemDto target) {
        if (source == null) {
            return null;
        }

        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setAvailable(source.getAvailable());
        target.setOwner(userMapper.toDto(source.getOwner()));

        if (source.getComments() != null) {
            target.setComments(commentMapper.toDto(source.getComments()));
        } else {
            target.setComments(new ArrayList<>());
        }

        target.setId(source.getId());

        return target;
    }

    @Override
    public Item fromDto(ItemDto source, Item target) {
        if (source == null) {
            return null;
        }

        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setAvailable(source.getAvailable());
        target.setOwner(userMapper.fromDto(source.getOwner()));

        target.setId(source.getId());

        return target;
    }

    @Override
    public ItemDto toDto(Item source) {
        return toDto(source, new ItemDto());
    }

    @Override
    public Item fromDto(ItemDto source) {
        return fromDto(source, new Item());
    }
}
