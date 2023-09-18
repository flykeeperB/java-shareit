package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Map;

public interface ExternalItemService {

    Item retrieve(Long itemId);

    Map<Long, List<Item>> retrieveForRequestsIds(List<Long> ids);

}
