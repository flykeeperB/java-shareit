package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.mapping.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.impl.TypesOfBookingConnectionToItem;
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
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.validators.BookerValidator;
import ru.practicum.shareit.item.validators.OnlyItemOwnerValidator;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.mapping.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository repository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;

    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private final BookingMapper bookingMapper;
    private final UserMapper userMapper;

    private final OnlyItemOwnerValidator onlyItemOwnerValidator;
    private final BookerValidator bookerValidator;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ItemDto create(CreateItemContext context) {

        User sharerUser = retrieveUser(context.getSharerUserId());

        ItemRequest itemRequest = null;
        if (context.getItemDto().getRequestId() != null) {
            itemRequest = retrieveItemRequest(context.getItemDto().getRequestId());
        }

        Item item = itemMapper.mapToItem(context.getItemDto(), sharerUser, itemRequest);

        ItemDto result = itemMapper.mapToItemDto(repository.save(item));

        result.setOwner(userMapper.mapToUserDto(item.getOwner()));

        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public ItemDto update(UpdateItemContext context) {
        log.info("обновление записи");

        User sharerUser = retrieveUser(context.getSharerUserId());

        Item oldItem = retrieve(context.getTargetItemId());

        onlyItemOwnerValidator.validate(context.getSharerUserId(), oldItem);

        //Если не переданы новые данные для обновления, просто возвращаем запись
        if (context.getItemDto() == null) {
            BasicItemContext request = BasicItemContext.builder()
                    .targetItemId(context.getTargetItemId())
                    .sharerUserId(context.getSharerUserId())
                    .build();

            return retrieve(request);
        }

        ItemRequest itemRequest = null;
        if (context.getItemDto().getRequestId() != null) {
            itemRequest = retrieveItemRequest(context.getItemDto().getRequestId());
        }

        Item newDataForItem = itemMapper.mapToItem(context.getItemDto(), sharerUser, itemRequest);

        Item newItem = patch(newDataForItem, oldItem);

        ItemDto result = itemMapper.mapToItemDto(repository.save(newItem));

        result.setOwner(userMapper.mapToUserDto(newItem.getOwner()));

        return result;
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

        itemMapper.mapToItemDto(item, result);

        result.setComments(commentMapper.mapToCommentDto(item.getComments()));

        result.setOwner(userMapper.mapToUserDto(item.getOwner()));

        if (context.getSharerUserId()
                .equals(item.getOwner().getId())) {
            itemMapper.mapToItemExtraDto(item, result);
            result.setNextBooking(bookingMapper.mapToBookingDto(item.getNextBooking()));
            result.setLastBooking(bookingMapper.mapToBookingDto(item.getLastBooking()));
        }

        return result;
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<ItemExtraDto> retrieveForOwner(RetrieveItemForOwnerContext context) {
        log.info("получение записей о вещах по идентификатору владельца");

        Pageable pageable = PageRequest.of(context.getFrom() / context.getSize(),
                context.getSize());

        List<Item> result = repository.findByOwnerIdOrderById(
                context.getSharerUserId(),
                pageable
        );

        setBookingsForItems(result);

        return itemMapper.mapToItemExtraDto(result, bookingMapper);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<ItemDto> retrieveAvailableForSearchText(
            RetrieveAvailableForSearchTextContext context) {
        log.info("поиск доступных для аренды вещей по тексту в наименовании или описании");

        Pageable pageable = PageRequest.of(context.getFrom() / context.getSize(),
                context.getSize());

        List<Item> result = repository.findAvailableByNameOrDescriptionContainingSearchText(
                context.getSearchText(),
                pageable
        );

        return itemMapper.mapToItemDto(result);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public CommentDto createComment(CreateCommentContext context) {
        log.info("добавление комментария");

        User sharerUser = retrieveUser(context.getSharerUserId());
        Item item = retrieve(context.getTargetItemId());

        List<Booking> bookings = bookingRepository
                .findByBookerIdAndStatusAndEndBefore(context.getSharerUserId(),
                        BookingStatus.APPROVED,
                        LocalDateTime.now());

        bookerValidator.validate(context.getSharerUserId(), bookings);

        Comment comment = commentMapper.mapToComment(context.getComment(),
                sharerUser,
                item);

        comment.setCreated(LocalDateTime.now());

        Comment result = commentRepository.save(comment);

        return commentMapper.mapToCommentDto(result);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Item retrieve(Long itemId) {
        Optional<Item> result = repository.findById(itemId);

        result.orElseThrow(() -> new NotFoundException("запись не найдена"));

        return result.get();
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    private Map<TypesOfBookingConnectionToItem, Booking> retrieveBookingForItem(Long itemId) {
        Map<TypesOfBookingConnectionToItem, Booking> result = new HashMap<>();
        LocalDateTime nowTime = LocalDateTime.now();
        List<Booking> bookings;

        bookings = bookingRepository.findByItemIdAndStatusAndStartBeforeOrderByEndDesc(itemId,
                BookingStatus.APPROVED,
                nowTime);

        if (bookings.size() > 0) {
            result.put(TypesOfBookingConnectionToItem.LAST, bookings.get(0));
        }

        bookings = bookingRepository.findFirstByItemIdAndStatusAndStartAfterOrderByStart(itemId,
                BookingStatus.APPROVED,
                nowTime);

        if (bookings.size() > 0) {
            result.put(TypesOfBookingConnectionToItem.NEXT, bookings.get(0));
        }

        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    private Map<Long, Map<TypesOfBookingConnectionToItem, Booking>> retrieveBookingsForItems(List<Long> ids) {
        Map<Long, Map<TypesOfBookingConnectionToItem, Booking>> result = new HashMap<>();
        LocalDateTime nowTime = LocalDateTime.now();

        groupBookingsByItems(result,
                bookingRepository.findLastBookingsForItems(ids,
                        BookingStatus.APPROVED,
                        nowTime),
                TypesOfBookingConnectionToItem.LAST);

        groupBookingsByItems(result,
                bookingRepository.findNextBookingsForItems(ids,
                        BookingStatus.APPROVED,
                        nowTime),
                TypesOfBookingConnectionToItem.NEXT);

        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    private ItemRequest retrieveItemRequest(Long id) {
        Optional<ItemRequest> result = itemRequestRepository.findById(id);

        result.orElseThrow(() -> new NotFoundException("запись не найдена"));

        return result.get();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    private void groupBookingsByItems(Map<Long, Map<TypesOfBookingConnectionToItem, Booking>> bookingByItemsId,
                                      List<Booking> bookings,
                                      TypesOfBookingConnectionToItem type) {
        if (bookings.size() > 0) {
            Long lastItemId = -1L;
            for (Booking booking : bookings) {
                Long itemId = booking.getItem().getId();

                if (!itemId.equals(lastItemId)) {
                    Map<TypesOfBookingConnectionToItem, Booking> bookingsForOneItem =
                            bookingByItemsId.getOrDefault(itemId, new HashMap<>());
                    bookingsForOneItem.put(type, booking);
                    bookingByItemsId.put(itemId, bookingsForOneItem);
                }
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    private void setBookingsForItem(Item target) {
        Map<TypesOfBookingConnectionToItem, Booking> bookings;

        bookings = retrieveBookingForItem(target.getId());

        target.setLastBooking(bookings.get(TypesOfBookingConnectionToItem.LAST));
        target.setNextBooking(bookings.get(TypesOfBookingConnectionToItem.NEXT));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    private void setBookingsForItems(List<Item> target) {
        Map<Long, Map<TypesOfBookingConnectionToItem, Booking>> bookingsByItems;

        bookingsByItems = retrieveBookingsForItems(target.stream()
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

    @Transactional(propagation = Propagation.REQUIRED)
    private void setCommentsForItem(Item item) {
        item.setComments(commentRepository.findByItemId(item.getId()));
    }

    @Transactional(propagation = Propagation.REQUIRED)
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

    @Transactional(propagation = Propagation.REQUIRED)
    private User retrieveUser(Long id) {
        Optional<User> result = userRepository.findById(id);

        result.orElseThrow(() -> new NotFoundException("запись о пользователе не найдена"));

        return result.get();
    }
}
