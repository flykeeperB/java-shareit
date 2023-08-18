package ru.practicum.shareit.item.mapping.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapping.ToCommentDtoMapper;
import ru.practicum.shareit.item.mapping.ToItemDtoMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapping.ToUserDtoMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ToItemDtoMapperImpl implements ToItemDtoMapper {

    private final ToUserDtoMapper userMapper;
    private final ToCommentDtoMapper toCommentDtoMapper;

    @Override
    public ItemDto map(Item item) {
        return map(item, new ItemDto());
    }

    @Override
    public ItemDto map(Item source, ItemDto target) {
        if (source == null) {
            return null;
        }

        target.setId(source.getId());
        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setAvailable(source.getAvailable());
        target.setOwner(userMapper.map(source.getOwner()));

        if (source.getComments() != null) {
            target.setComments(toCommentDtoMapper.map(source.getComments()));
        } else {
            target.setComments(new ArrayList<>());
        }

        if (source.getRequest() != null) {
            target.setRequestId(source.getRequest().getId());
        }

        return target;
    }

    @Override
    public List<ItemDto> map(List<Item> source) {
        return source
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

}
