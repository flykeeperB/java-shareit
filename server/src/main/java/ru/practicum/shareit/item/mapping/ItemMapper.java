package ru.practicum.shareit.item.mapping;

import ru.practicum.shareit.booking.mapping.BookingMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemExtraDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemMapper {

    ItemDto mapToItemDto(Item source);

    ItemDto mapToItemDto(Item source, ItemDto target);

    List<ItemDto> mapToItemDto(List<Item> source);

    ItemExtraDto mapToItemExtraDto(Item source);

    ItemExtraDto mapToItemExtraDto(Item source, ItemExtraDto target);

    List<ItemExtraDto> mapToItemExtraDto(List<Item> source);

    List<ItemExtraDto> mapToItemExtraDto(List<Item> source, BookingMapper bookingMapper);

    Item mapToItem(ItemDto itemDto, User sharerUser, ItemRequest itemRequest);

}
