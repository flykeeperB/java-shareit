package ru.practicum.shareit.item.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.AbstractService;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.TypesOfBookingConnectionToItem;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.ItemValidator;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ItemServiceImpl
        extends AbstractService<Item, ItemValidator>
        implements ItemService {

    private final ItemRepository repository;
    private final UserService userService;
    private final BookingService bookingService;
    private final CommentRepository commentRepository;

    @Autowired
    @Lazy
    public ItemServiceImpl(ItemRepository repository,
                           UserService userService,
                           ItemValidator validator,
                           BookingService bookingService,
                           CommentRepository commentRepository) {
        super(repository, validator);
        this.repository = repository;
        this.userService = userService;
        this.bookingService = bookingService;
        this.commentRepository = commentRepository;
    }

    @Override
    public String getName() {
        return "Items";
    }

    @Override
    protected Item patch(Item source, Item target) {
        Item result = super.patch(source, target);

        if (source.getName() != null) {
            result.setName(source.getName());
        }

        if (source.getDescription() != null) {
            result.setDescription(source.getDescription());
        }

        if (source.getAvailable() != null) {
            result.setAvailable(source.getAvailable());
        }

        return result;
    }

    protected void setBookingsForItem(Item target) {
        Map<TypesOfBookingConnectionToItem, Booking> bookings;

        bookings = bookingService.retrieveForItem(target.getId());

        target.setLastBooking(bookings.get(TypesOfBookingConnectionToItem.LAST));
        target.setNextBooking(bookings.get(TypesOfBookingConnectionToItem.NEXT));
    }

    protected void setCommentsForItem(Item item) {
        item.setComments(commentRepository.findByItemId(item.getId()));
    }

    protected Item setOwnerOfItemFromUserId(Item target, Optional<Long> userId) {

        if (userId.isPresent()) {
            target.setOwner(userService.retrieve(userId.get(), userId));
        }

        return target;
    }

    @Override
    public Item create(Item source, Optional<Long> userId) {
        validator.validateUserId(userId);

        source = setOwnerOfItemFromUserId(source, userId);

        return repository.save(source);
    }

    @Override
    public Item update(Item source, Optional<Long> userId) {
        logInfo("обновление записи");

        validator.validateUserId(userId);

        source = setOwnerOfItemFromUserId(source, userId);

        Item target = retrieve(source.getId(), userId);

        validator.validateOwnerChange(source, target);

        target = patch(source, target);

        return repository.save(target);
    }

    @Override
    public Item retrieve(Long id, Optional<Long> userId) {
        Item result = super.retrieve(id, userId);

        this.setBookingsForItem(result);
        this.setCommentsForItem(result);

        return result;
    }

    @Override
    public List<Item> retrieveForOwner(Optional<Long> userId) {
        logInfo("получение записей о вещах по идентификатору владельца");

        this.validator.forRetrieveForOwner(userId);

        List<Item> result = repository.findByOwnerId(userId.get());

        result.forEach(this::setBookingsForItem);

        return result;
    }

    @Override
    public List<Item> retrieveAvailableForSearchText(String searchText, Optional<Long> userId) {
        logInfo("поиск доступных для аренды вещей по тексту в наименовании или описании");

        if (searchText.isEmpty()) {
            return new ArrayList<>();
        }

        this.validator.forRetrieveAvailableForSearchText(searchText, userId);

        return repository.findAvailableByNameOrDescriptionContainingSearchText(searchText);
    }

    @Override
    public Comment createComment(Comment source, Long itemId, Optional<Long> userId) {
        logInfo("добавление комментария");

        List<Booking> completedBookingsOfAuthor = bookingService.retrieveForBooker(userId, State.COMPLETED);

        Optional<Item> item = repository.findById(itemId);

        item.ifPresent(source::setItem);

        if (userId.isPresent()) {
            source.setAuthor(userService.retrieve(userId.get(), userId));
        }

        source.setCreated(LocalDateTime.now());

        validator.forCreateComment(source, completedBookingsOfAuthor);

        return commentRepository.save(source);
    }
}
