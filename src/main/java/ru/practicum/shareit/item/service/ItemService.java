package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.contexts.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemExtraDto;

import java.util.List;

public interface ItemService {
    List<ItemExtraDto> retrieveForOwner(RetrieveItemForOwnerContext context);

    List<ItemDto> retrieveAvailableForSearchText(RetrieveAvailableForSearchTextContext context);

    CommentDto createComment(CreateCommentContext context);

    ItemDto create(CreateItemContext context);

    ItemExtraDto retrieve(BasicItemContext context);

    ItemDto update(UpdateItemContext context);

    void delete(BasicItemContext context);

}
