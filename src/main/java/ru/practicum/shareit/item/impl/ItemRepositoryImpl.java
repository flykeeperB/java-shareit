package ru.practicum.shareit.item.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.InMemoryRepository;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Component
public class ItemRepositoryImpl
        extends InMemoryRepository<Item>
        implements ItemRepository {

    private final Map<Long, Set<Long>> itemsByOwner = new HashMap<>();

    public ItemRepositoryImpl() {
        super("Вещи");
    }

    @Override
    protected Item patch(Item source, Item target) {
        Item result = super.patch(source, new Item(target));

        if (source.getName() != null) {
            result.setName(source.getName());
        }

        if (source.getDescription() != null) {
            result.setDescription(source.getDescription());
        }

        if (source.getAvailable() != null) {
            result.setAvailable(source.getAvailable());
        }

        return result;
    }

    @Override
    public Item create(Item source) {
        Item result = super.create(source);

        Set<Long> ids = itemsByOwner.getOrDefault(result.getOwner().getId(), new HashSet<>());
        ids.add(result.getId());
        itemsByOwner.put(result.getOwner().getId(), ids);

        return result;
    }

    @Override
    public Item update(Item source) {
        Item result = super.update(new Item(source));

        return result;
    }

    @Override
    public List<Item> retrieveForOwner(Long userId) {
        List<Long> ids = itemsByOwner.getOrDefault(userId, new HashSet<>()).stream().toList();

        return retrieve(ids);
    }

    @Override
    public List<Item> retrieveForSearchText(String searchText) {
        List<Item> result = new ArrayList<>();

        if (searchText.isEmpty()) {
            return result;
        }

        String prepearedSearchText = searchText.toUpperCase();

        for (Item value : this.storage.values()) {
            boolean fits = value.getName().toUpperCase().indexOf(prepearedSearchText) > -1;

            if (value.getDescription().toUpperCase().indexOf(prepearedSearchText) > -1) {
                fits = true;
            }
            if (!value.getAvailable()) {
                fits = false;
            }
            if (fits) {
                result.add(value);
            }
        }

        return result;
    }

    @Override
    public void delete(Long id) {
        Item item = retrieve(id);

        itemsByOwner.get(item.getOwner().getId()).remove(item.getId());

        super.delete(id);
    }
}
