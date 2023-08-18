package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.model.ItemRequest;

public interface ExternalItemRequestService {

    ItemRequest retrieve(Long id);

}
