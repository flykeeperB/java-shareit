package ru.practicum.shareit.item.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.AbstractService;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Service
public class ItemServiceImpl
        extends AbstractService<Item>
        implements ItemService {

    private final ItemRepository repository;

    @Autowired
    public ItemServiceImpl(ItemRepository repository) {
        super(repository, "Items");
        this.repository = repository;
    }

    @Override
    public List<Item> retrieveForOwner(Long userId) {
        logInfo("получение записей о вещах по идентификатору владельца");

        return repository.retrieveForOwner(userId);
    }

    @Override
    public List<Item> retrieveAvailableForSearchText(String searchText) {
        logInfo("поиск доступных для аренды вещей по тексту в наименовании или описании");

        return repository.retrieveForSearchText(searchText);
    }
}
