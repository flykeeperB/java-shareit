package ru.practicum.shareit.request.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.mapping.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.contexts.RetrieveItemRequestContext;
import ru.practicum.shareit.request.contexts.RetrieveItemRequestsContext;
import ru.practicum.shareit.request.contexts.RetrieveItemRequestsForUserContext;
import ru.practicum.shareit.request.dto.ExtraItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.contexts.CreateItemRequestContext;
import ru.practicum.shareit.request.mapping.*;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.mapping.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository repository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    private final ItemRequestMapper itemRequestMapper;
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ItemRequestDto create(CreateItemRequestContext context) {
        log.info("создание запроса о предоставлении вещи");

        User sharerUser = retrieveUser(context.getSharerUserId());

        ItemRequest itemRequest = repository.save(itemRequestMapper
                .mapToItemRequest(context.getItemRequestDto(), sharerUser));

        ItemRequestDto result = itemRequestMapper.mapToItemRequestDto(itemRequest);
        result.setRequestor(userMapper.mapToUserDto(itemRequest.getRequestor()));

        return itemRequestMapper.mapToItemRequestDto(itemRequest);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<ExtraItemRequestDto> retrieve(RetrieveItemRequestsForUserContext context) {
        log.info("получение списка запросов о предоставлении вещей, созданных отдельным пользователем");

        retrieveUser(context.getSharerUserId()); //Выбросит исключение, если пользователь не найден

        List<ItemRequest> requests = repository.findRequestByRequestorIdOrderByCreatedDesc(
                context.getSharerUserId()
        );

        setItemsToItemRequests(requests);

        return itemRequestMapper.mapToExtraItemRequestDto(requests, itemMapper);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<ExtraItemRequestDto> retrieve(RetrieveItemRequestsContext context) {
        log.info("получение общего списка запросов о преодставлении вещей (постранично)");

        retrieveUser(context.getSharerUserId()); //Выбросит исключение, если пользователь не найден

        Pageable pageable = PageRequest.of(context.getFrom() / context.getSize(),
                context.getSize());
        Page<ItemRequest> requests = repository.findByRequestorIdNot(context.getSharerUserId(), pageable);

        setItemsToItemRequests(requests.toList());

        return itemRequestMapper.mapToExtraItemRequestDto(requests.toList(), itemMapper);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ExtraItemRequestDto retrieve(RetrieveItemRequestContext context) {
        log.info("получение сведений о конкретном запросе о предоставлении вещи");

        retrieveUser(context.getSharerUserId()); //Выбросит исключение, если пользователь не найден

        ItemRequest request = repository.findById(context
                        .getTargetItemRequestId())
                .orElseThrow();

        setItemsToItemRequests(List.of(request));

        ExtraItemRequestDto result = itemRequestMapper.mapToExtraItemRequestDto(request);
        result.setRequestor(userMapper.mapToUserDto(request.getRequestor()));
        result.setItems(request
                .getItems()
                .stream()
                .map(itemMapper::mapToItemDto)
                .collect(Collectors.toList()));

        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    private Map<Long, List<Item>> retrieveForRequestsIds(List<Long> ids) {
        List<Item> items = itemRepository.findByRequestIdInOrderByRequestId(ids);

        return items.stream().collect(Collectors.groupingBy(item -> item.getRequest().getId()));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    private void setItemsToItemRequests(List<ItemRequest> requests) {
        Map<Long, List<Item>> items = retrieveForRequestsIds(requests
                .stream()
                .map(ItemRequest::getId)
                .collect(Collectors.toList()));

        requests.forEach(itemRequest -> {
            itemRequest.setItems(items.get(itemRequest.getId()));
            if (itemRequest.getItems() == null) {
                itemRequest.setItems(new ArrayList<>());
            }
        });
    }

    @Transactional(propagation = Propagation.REQUIRED)
    private User retrieveUser(Long id) {
        Optional<User> result = userRepository.findById(id);

        result.orElseThrow(() -> new NotFoundException("запись о пользователе не найдена"));

        return result.get();
    }
}
