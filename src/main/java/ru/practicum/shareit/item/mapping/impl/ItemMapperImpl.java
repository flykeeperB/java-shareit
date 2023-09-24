package ru.practicum.shareit.item.mapping.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.mapping.BookingMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemExtraDto;
import ru.practicum.shareit.item.mapping.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemMapperImpl implements ItemMapper {

    @Override
    public ItemDto mapToItemDto(Item source) {
        return mapToItemDto(source, new ItemDto());
    }

    @Override
    public ItemDto mapToItemDto(Item source, ItemDto target) {
        if (source == null) {
            return null;
        }

        target.setId(source.getId());
        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setAvailable(source.getAvailable());

        if (source.getComments() != null) {
            target.setComments(mapToCommentDto(source.getComments()));
        } else {
            target.setComments(new ArrayList<>());
        }

        if (source.getRequest() != null) {
            target.setRequestId(source.getRequest().getId());
        }

        return target;
    }

    @Override
    public List<ItemDto> mapToItemDto(List<Item> source) {
        return source
                .stream()
                .map(this::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemExtraDto mapToItemExtraDto(Item source) {
        return mapToItemExtraDto(source, new ItemExtraDto());
    }

    @Override
    public ItemExtraDto mapToItemExtraDto(Item source, ItemExtraDto target) {
        if (source == null) {
            return null;
        }

        mapToItemDto(source, target);

        return target;
    }

    @Override
    public List<ItemExtraDto> mapToItemExtraDto(List<Item> source) {
        return source
                .stream()
                .map(this::mapToItemExtraDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemExtraDto> mapToItemExtraDto(List<Item> source, BookingMapper bookingMapper) {
        List<ItemExtraDto> result = new ArrayList<>();
        for (Item item : source) {
            ItemExtraDto itemExtraDto = mapToItemExtraDto(item);
            itemExtraDto.setNextBooking(bookingMapper.mapToBookingDto(item.getNextBooking()));
            itemExtraDto.setLastBooking(bookingMapper.mapToBookingDto(item.getLastBooking()));
            result.add(itemExtraDto);
        }

        return result;
    }

    @Override
    public Item mapToItem(ItemDto itemDto, User sharerUser, ItemRequest itemRequest) {
        Item target = new Item();
        target.setName(itemDto.getName());
        target.setDescription(itemDto.getDescription());
        target.setAvailable(itemDto.getAvailable());
        target.setOwner(sharerUser);
        target.setRequest(itemRequest);

        target.setId(itemDto.getId());
        return target;
    }

    @Override
    public CommentDto mapToCommentDto(Comment source) {
        if (source == null) {
            return null;
        }

        CommentDto target = new CommentDto();

        target.setCreated(source.getCreated());
        target.setAuthorName(source.getAuthor().getName());
        target.setText(source.getText());
        target.setId(source.getId());

        return target;
    }

    @Override
    public List<CommentDto> mapToCommentDto(List<Comment> source) {
        return source
                .stream()
                .map(this::mapToCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public Comment mapToComment(CommentDto commentDto, User sharerUser, Item item) {
        Comment target = new Comment();

        target.setText(commentDto.getText());
        target.setItem(item);
        target.setAuthor(sharerUser);

        target.setId(commentDto.getId());
        return target;
    }
}
