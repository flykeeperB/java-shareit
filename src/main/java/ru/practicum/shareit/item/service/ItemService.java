package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemExtraDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.requestsModels.*;

import java.util.List;

public interface ItemService {

    List<ItemExtraDto> retrieveForOwner(RetrieveItemForOwnerRequest request);

    List<ItemDto> retrieveAvailableForSearchText(RetrieveAvailableForSearchTextRequest request);

    CommentDto createComment(CreateCommentRequestBasic request);

    ItemDto create(CreateItemRequest request);

    ItemExtraDto retrieve(BasicItemRequest request);

    ItemDto update(UpdateBasicItemRequest updateItemRequest);

    void delete(BasicItemRequest BasicItemRequest);

    Item retrieve(Long itemId);

}
