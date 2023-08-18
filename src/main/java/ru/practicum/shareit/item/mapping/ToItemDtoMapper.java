package ru.practicum.shareit.item.mapping;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ToItemDtoMapper {

    ItemDto map (Item source);

    ItemDto map (Item source, ItemDto target);

    List<ItemDto> map (List<Item> source);

}
