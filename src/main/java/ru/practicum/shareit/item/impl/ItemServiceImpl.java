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
        super(repository);
        this.repository = repository;
    }

    @Override
    public List<Item> retrieveForOwner(Long userId) {
        return repository.retrieveForOwner(userId);
    }

    @Override
    public List<Item> retrieveForSearchText(String searchText) {
        return repository.retrieveForSearchText(searchText);
    }
}
