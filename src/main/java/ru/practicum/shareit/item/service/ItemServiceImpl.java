package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.requestsModels.ForStateBookingRequest;
import ru.practicum.shareit.booking.service.TypesOfBookingConnectionToItem;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemExtraDto;
import ru.practicum.shareit.item.mapping.CommentMapper;
import ru.practicum.shareit.item.mapping.ItemExtraMapper;
import ru.practicum.shareit.item.mapping.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.requestsModels.*;
import ru.practicum.shareit.item.validators.BookerValidator;
import ru.practicum.shareit.item.validators.OnlyItemOwnerValidator;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository repository;
    private final UserService userService;
    private final BookingService bookingService;
    private final CommentRepository commentRepository;
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private final ItemExtraMapper itemExtraMapper;

    private final OnlyItemOwnerValidator onlyItemOwnerValidator;
    private final BookerValidator bookerValidator;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ItemDto create(CreateItemRequest createItemRequest) {

        Item item = itemMapper.fromDto(createItemRequest.getItemDto());

        item.setOwner(userService.retrieve(
                createItemRequest.getSharerUserId()));

        return itemMapper.toDto(repository.save(item));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ItemDto update(UpdateBasicItemRequest updateItemRequest) {
        log.info("обновление записи");

        Item item = retrieve(updateItemRequest.getTargetItemId());

        onlyItemOwnerValidator.validate(updateItemRequest, item);

        //Если не переданы новые данные для обновления, просто возвращаем запись
        if (updateItemRequest.getItemDto() == null) {
            BasicItemRequest request = BasicItemRequest.builder()
                    .targetItemId(updateItemRequest.getTargetItemId())
                    .sharerUserId(updateItemRequest.getSharerUserId())
                    .build();

            return retrieve(request);
        }

        Item newDataForItem = itemMapper.fromDto(updateItemRequest.getItemDto());

        patch(newDataForItem, item);

        return itemMapper.toDto(repository.save(item));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(BasicItemRequest request) {
        log.info("удаление записи");

        repository.deleteById(request.getTargetItemId());
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public ItemExtraDto retrieve(BasicItemRequest request) {
        log.info("получение записи по идентификатору");

        Item item = retrieve(request.getTargetItemId());

        this.setBookingsForItem(item);
        this.setCommentsForItem(item);

        ItemExtraDto result = new ItemExtraDto();

        if (request.getSharerUserId()
                .equals(item.getOwner().getId())) {
            itemExtraMapper.toDto(item, result);
        } else {
            itemMapper.toDto(item, result);
        }

        return result;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<ItemExtraDto> retrieveForOwner(RetrieveItemForOwnerRequest retrieveItemForOwnerRequest) {
        log.info("получение записей о вещах по идентификатору владельца");

        List<Item> result = repository.findByOwnerId(
                retrieveItemForOwnerRequest.getSharerUserId()
        );

        setBookingsForItems(result);

        return itemExtraMapper.toDto(result);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<ItemDto> retrieveAvailableForSearchText(
            RetrieveAvailableForSearchTextRequest retrieveAvailableForSearchTextRequest) {
        log.info("поиск доступных для аренды вещей по тексту в наименовании или описании");

        if (retrieveAvailableForSearchTextRequest.getSearchText().isEmpty()) {
            return new ArrayList<>();
        }

        List<Item> result = repository.findAvailableByNameOrDescriptionContainingSearchText(
                retrieveAvailableForSearchTextRequest.getSearchText()
        );

        return itemMapper.toDto(result);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public CommentDto createComment(CreateCommentRequestBasic request) {
        log.info("добавление комментария");

        ForStateBookingRequest retrieveRequest = ForStateBookingRequest.builder()
                .sharerUserId(request.getSharerUserId())
                .state(State.COMPLETED)
                .build();

        List<BookingDto> successfulBookings = bookingService.retrieveForBooker(
                retrieveRequest
        );

        bookerValidator.validate(request, successfulBookings);

        User user = userService.retrieve(request.getSharerUserId());
        Item item = retrieve(request.getTargetItemId());

        Comment comment = commentMapper.fromDto(request.getComment());

        comment.setItem(item);
        comment.setAuthor(user);

        comment.setCreated(LocalDateTime.now());

        Comment result = commentRepository.save(comment);

        return commentMapper.toDto(result);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Item retrieve(Long itemId) {
        Optional<Item> result = repository.findById(itemId);

        result.orElseThrow(() -> new NotFoundException("запись не найдена"));

        return result.get();
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

    private void patch(Item source, Item target) {

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
    }
}
