package ru.practicum.shareit.request.mapping.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.mapping.ToItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.contexts.CreateItemRequestContext;

import java.time.LocalDateTime;

@Component
public class ToItemRequestMapperImpl implements ToItemRequestMapper {

    @Override
    public ItemRequest map(CreateItemRequestContext context) {
        ItemRequest itemRequest = new ItemRequest();

        itemRequest.setDescription(context.getCreateItemRequestDto().getDescription());
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequestor(context.getSharerUser());

        return itemRequest;
    }

}
