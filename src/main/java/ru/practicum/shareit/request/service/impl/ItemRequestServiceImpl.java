package ru.practicum.shareit.request.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.core.validators.SharerUserValidator;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ExternalItemService;
import ru.practicum.shareit.request.dto.ExtraItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.contexts.CreateItemRequestContext;
import ru.practicum.shareit.request.contexts.RetrieveItemRequestContext;
import ru.practicum.shareit.request.contexts.RetrieveItemRequestsForUserContext;
import ru.practicum.shareit.request.contexts.RetrieveItemRequestsContext;
import ru.practicum.shareit.request.mapping.ToExtraItemRequestDtoListMapper;
import ru.practicum.shareit.request.mapping.ToExtraItemRequestDtoMapper;
import ru.practicum.shareit.request.mapping.ToItemRequestDtoMapper;
import ru.practicum.shareit.request.mapping.ToItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ControllerItemRequestService;
import ru.practicum.shareit.request.service.ExternalItemRequestService;
import ru.practicum.shareit.request.validators.NotBlankDescriptionOfItemRequestValidator;
import ru.practicum.shareit.user.service.ExternalUserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ControllerItemRequestService, ExternalItemRequestService {

    private final ItemRequestRepository repository;

    private final SharerUserValidator sharerUserValidator;
    private final NotBlankDescriptionOfItemRequestValidator notBlankDescriptionOfItemRequestValidator;

    private final ToItemRequestMapper toItemRequestMapper;
    private final ToItemRequestDtoMapper toItemRequestDtoMapper;
    private final ToExtraItemRequestDtoMapper toExtraItemRequestDtoMapper;
    private final ToExtraItemRequestDtoListMapper toExtraItemRequestDtoListMapper;

    @Lazy
    private final ExternalUserService userService;

    @Lazy
    private final ExternalItemService itemService;


    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ItemRequestDto create(CreateItemRequestContext context) {
        log.info("создание запроса о предоставлении вещи");

        context.setSharerUser(userService.retrieve(context.getSharerUserId()));

        sharerUserValidator.validate(context);
        notBlankDescriptionOfItemRequestValidator.validate(context);

        ItemRequest itemRequest = repository.save(toItemRequestMapper.map(context));

        return toItemRequestDtoMapper.map(itemRequest);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<ExtraItemRequestDto> retrieve(RetrieveItemRequestsForUserContext context) {
        log.info("получение списка запросов о предоставлении вещей, созданных отдельным пользователем");

        context.setSharerUser(userService.retrieve(context.getSharerUserId()));

        sharerUserValidator.validate(context);

        List<ItemRequest> requests = repository.findRequestByRequestorIdOrderByCreatedDesc(
                context.getSharerUserId()
        );

        setItemsToItemRequests(requests);

        return toExtraItemRequestDtoListMapper.map(requests);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<ExtraItemRequestDto> retrieve(RetrieveItemRequestsContext context) {
        log.info("получение общего списка запросов о преодставлении вещей (постранично)");

        context.setSharerUser(userService.retrieve(context.getSharerUserId()));

        sharerUserValidator.validate(context);

        Pageable pageable = PageRequest.of(context.getFrom() / context.getSize(),
                context.getSize());
        Page<ItemRequest> requests = repository.findAll(context.getSharerUserId(), pageable);

        setItemsToItemRequests(requests.toList());

        return toExtraItemRequestDtoListMapper.map(requests.toList());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ExtraItemRequestDto retrieve(RetrieveItemRequestContext context) {
        log.info("получение сведений о конкретном запросе о предоставлении вещи");

        context.setSharerUser(userService.retrieve(context.getSharerUserId()));

        sharerUserValidator.validate(context);

        ItemRequest request = repository.findById(context
                        .getTargetItemRequestId())
                .orElseThrow();

        setItemsToItemRequests(List.of(request));

        return toExtraItemRequestDtoMapper.map(request);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ItemRequest retrieve(Long id) {
        Optional<ItemRequest> result = repository.findById(id);

        result.orElseThrow(() -> new NotFoundException("запись не найдена"));

        return result.get();
    }

    private void setItemsToItemRequests(List<ItemRequest> requests) {
        Map<Long, List<Item>> items = itemService
                .retrieveForRequestsIds(requests
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
}
