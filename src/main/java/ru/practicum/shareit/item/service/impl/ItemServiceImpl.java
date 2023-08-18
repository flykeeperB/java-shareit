package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.ExternalBookingService;
import ru.practicum.shareit.booking.service.impl.TypesOfBookingConnectionToItem;
import ru.practicum.shareit.core.validators.SharerUserValidator;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemExtraDto;
import ru.practicum.shareit.item.mapping.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.contexts.*;
import ru.practicum.shareit.item.service.ControllerItemService;
import ru.practicum.shareit.item.service.ExternalItemService;
import ru.practicum.shareit.item.validators.BookerValidator;
import ru.practicum.shareit.item.validators.OnlyItemOwnerValidator;
import ru.practicum.shareit.request.service.ExternalItemRequestService;
import ru.practicum.shareit.user.service.ExternalUserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ExternalItemService, ControllerItemService {

    private final ItemRepository repository;
    private final CommentRepository commentRepository;

    private final ToItemDtoMapper toItemDtoMapper;
    private final ToItemExtraDtoMapper toItemExtraDtoMapper;
    private final ToItemMapper toItemMapper;
    private final ToCommentMapper toCommentMapper;
    private final ToCommentDtoMapper toCommentDtoMapper;

    private final OnlyItemOwnerValidator onlyItemOwnerValidator;
    private final BookerValidator bookerValidator;
    private final SharerUserValidator sharerUserValidator;

    @Lazy
    private final ExternalUserService userService;

    @Lazy
    private final ExternalBookingService bookingService;

    @Lazy
    private final ExternalItemRequestService itemRequestService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ItemDto create(CreateItemContext context) {

        context.setSharerUser(userService.retrieve(context.getSharerUserId()));

        if (context.getItemDto().getRequestId() != null) {
            context.setItemRequest(
                    itemRequestService.retrieve(context.getItemDto().getRequestId())
            );
        }

        sharerUserValidator.validate(context);

        Item item = toItemMapper.map(context);

        return toItemDtoMapper.map(repository.save(item));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ItemDto update(UpdateItemContext context) {
        log.info("обновление записи");

        context.setOldItem(retrieve(context.getTargetItemId()));

        onlyItemOwnerValidator.validate(context);

        //Если не переданы новые данные для обновления, просто возвращаем запись
        if (context.getItemDto() == null) {
            BasicItemContext request = BasicItemContext.builder()
                    .targetItemId(context.getTargetItemId())
                    .sharerUserId(context.getSharerUserId())
                    .build();

            return retrieve(request);
        }

        Item newDataForItem = toItemMapper.map(context);

        Item newItem = patch(newDataForItem, context.getOldItem());

        return toItemDtoMapper.map(repository.save(newItem));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(BasicItemContext context) {
        log.info("удаление записи");

        repository.deleteById(context.getTargetItemId());
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public ItemExtraDto retrieve(BasicItemContext context) {
        log.info("получение записи по идентификатору");

        Item item = retrieve(context.getTargetItemId());

        this.setBookingsForItem(item);
        this.setCommentsForItem(item);

        ItemExtraDto result = new ItemExtraDto();

        toItemDtoMapper.map(item, result);

        if (context.getSharerUserId()
                .equals(item.getOwner().getId())) {
            toItemExtraDtoMapper.map(item, result);
        }

        return result;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<ItemExtraDto> retrieveForOwner(RetrieveItemForOwnerContext context) {
        log.info("получение записей о вещах по идентификатору владельца");

        Pageable pageable = PageRequest.of(context.getFrom() / context.getSize(),
                context.getSize());

        List<Item> result = repository.findByOwnerId(
                context.getSharerUserId(),
                pageable
        );

        setBookingsForItems(result);

        return toItemExtraDtoMapper.map(result);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<ItemDto> retrieveAvailableForSearchText(
            RetrieveAvailableForSearchTextContext context) {
        log.info("поиск доступных для аренды вещей по тексту в наименовании или описании");

        if (context.getSearchText().isEmpty()) {
            return new ArrayList<>();
        }

        Pageable pageable = PageRequest.of(context.getFrom() / context.getSize(),
                context.getSize());

        List<Item> result = repository.findAvailableByNameOrDescriptionContainingSearchText(
                context.getSearchText(),
                pageable
        );

        return toItemDtoMapper.map(result);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public CommentDto createComment(CreateCommentContext context) {
        log.info("добавление комментария");

        context.setSharerUser(userService.retrieve(context.getSharerUserId()));

        context.setSuccessfulBookings(bookingService
                .retrieveSuccessfulBookings(context.getSharerUserId()));

        bookerValidator.validate(context);

        context.setItem(retrieve(context.getTargetItemId()));

        Comment comment = toCommentMapper.map(context);

        comment.setCreated(LocalDateTime.now());

        return toCommentDtoMapper.map(commentRepository.save(comment));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Item retrieve(Long itemId) {
        Optional<Item> result = repository.findById(itemId);

        result.orElseThrow(() -> new NotFoundException("запись не найдена"));

        return result.get();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Map<Long, List<Item>> retrieveForRequestsIds(List<Long> ids) {
        List<Item> items = repository.findByRequestIdInOrderByRequestId(ids);

        return items.stream().collect(Collectors.groupingBy(item -> item.getRequest().getId()));
    }

    private void setBookingsForItem(Item target) {
        Map<TypesOfBookingConnectionToItem, Booking> bookings;

        bookings = bookingService.retrieveForItem(target.getId());

        target.setLastBooking(bookings.get(TypesOfBookingConnectionToItem.LAST));
        target.setNextBooking(bookings.get(TypesOfBookingConnectionToItem.NEXT));
    }

    private void setBookingsForItems(List<Item> target) {
        Map<Long, Map<TypesOfBookingConnectionToItem, Booking>> bookingsByItems;

        bookingsByItems = bookingService.retrieveForItems(target.stream()
                .map(Item::getId)
                .collect(Collectors.toList()));

        for (Item item : target) {
            Map<TypesOfBookingConnectionToItem, Booking> bookings = bookingsByItems.get(item.getId());
            if (bookings != null) {
                item.setLastBooking(bookings.get(TypesOfBookingConnectionToItem.LAST));
                item.setNextBooking(bookings.get(TypesOfBookingConnectionToItem.NEXT));
            }
        }

    }

    private void setCommentsForItem(Item item) {
        item.setComments(commentRepository.findByItemId(item.getId()));
    }

    private Item patch(Item source, Item target) {

        if (source.getId() != null) {
            target.setId(source.getId());
        }

        if (source.getName() != null) {
            target.setName(source.getName());
        }

        if (source.getDescription() != null) {
            target.setDescription(source.getDescription());
        }

        if (source.getAvailable() != null) {
            target.setAvailable(source.getAvailable());
        }

        return target;
    }
}
