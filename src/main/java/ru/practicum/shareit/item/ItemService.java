package ru.practicum.shareit.item;

import ru.practicum.shareit.Service;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService extends Service<Item, ItemValidator> {

    List<Item> retrieveForOwner(Optional<Long> userId);

    List<Item> retrieveAvailableForSearchText(String searchText, Optional<Long> userId);

    Comment createComment(Comment source, Long itemId, Optional<Long> userId);

}
