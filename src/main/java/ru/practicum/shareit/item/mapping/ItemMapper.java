package ru.practicum.shareit.item.mapping;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapping.UserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ItemMapper {

    private final UserMapper userMapper;
    private final CommentMapper commentMapper;

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

    public ItemDto toDto(Item source) {
        return toDto(source, new ItemDto());
    }

    public Item fromDto(ItemDto source) {
        return fromDto(source, new Item());
    }

    public List<ItemDto> toDto(List<Item> source) {
        return source
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
