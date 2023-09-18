package ru.practicum.shareit.item.mapping;

import ru.practicum.shareit.item.dto.ItemExtraDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ToItemExtraDtoMapper {

    ItemExtraDto map(Item source);

    ItemExtraDto map(Item source, ItemExtraDto target);

    List<ItemExtraDto> map(List<Item> source);

}
