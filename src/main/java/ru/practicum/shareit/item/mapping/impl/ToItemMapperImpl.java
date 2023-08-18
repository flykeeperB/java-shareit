package ru.practicum.shareit.item.mapping.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.contexts.CreateItemContext;
import ru.practicum.shareit.item.mapping.ToItemMapper;
import ru.practicum.shareit.item.model.Item;

@Component
public class ToItemMapperImpl implements ToItemMapper {

    @Override
    public Item map(CreateItemContext context) {
        Item target = new Item();
        target.setName(context.getItemDto().getName());
        target.setDescription(context.getItemDto().getDescription());
        target.setAvailable(context.getItemDto().getAvailable());
        target.setOwner(context.getSharerUser());
        target.setRequest(context.getItemRequest());

        target.setId(context.getItemDto().getId());
        return target;
    }
}
