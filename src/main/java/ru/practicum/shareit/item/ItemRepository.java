package ru.practicum.shareit.item;

import ru.practicum.shareit.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends Repository<Item> {

    List<Item> retrieveForOwner(Long userId);

    List<Item> retrieveForSearchText(String searchText);

}
