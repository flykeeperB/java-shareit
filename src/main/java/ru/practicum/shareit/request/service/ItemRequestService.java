package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ExtraItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.contexts.CreateItemRequestContext;
import ru.practicum.shareit.request.contexts.RetrieveItemRequestContext;
import ru.practicum.shareit.request.contexts.RetrieveItemRequestsForUserContext;
import ru.practicum.shareit.request.contexts.RetrieveItemRequestsContext;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto create(CreateItemRequestContext context);

    List<ExtraItemRequestDto> retrieve(RetrieveItemRequestsForUserContext context);

    List<ExtraItemRequestDto> retrieve(RetrieveItemRequestsContext context);

    ExtraItemRequestDto retrieve(RetrieveItemRequestContext context);

}
