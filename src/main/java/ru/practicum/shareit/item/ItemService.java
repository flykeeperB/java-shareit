package ru.practicum.shareit.item;

import ru.practicum.shareit.Service;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService extends Service<Item> {
    List<Item> retrieveForOwner(Long userId);
    List<Item> retrieveForSearchText(String searchText);
}
