package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.BasicValidator;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

@Component
public class ItemValidator extends BasicValidator<Item> {

    @Override
    public String getName() {
        return "Items validator";
    }

    @Override
    protected void validateBeforeSave(Item source, Optional<Long> userId) {
        super.validateBeforeSave(source, userId);

        if (userId.isEmpty()) {
            throwException("анонимное добавление вещей не допускается");
        } else {
            if (!userId.get().equals(source.getOwner().getId())) {
                throwException("не допускается создание (сохрание) записи о вещи не владельцем");
            }
        }

        if (source.getOwner() == null) {
            throwException("не указан владелец вещи");
        }

    }

    public void forRetrieveForOwner(Optional<Long> userId) {
        validateUserId(userId);
    }

    public void validateOwnerChange(Item source, Item target) {
        if (!source.getOwner().getId().equals(target.getOwner().getId())) {
            throwException("изменение владельца вещи не допускается");
        }
    }

    public void forRetrieveAvailableForSearchText(String searchText, Optional<Long> userId) {
        validateUserId(userId);
    }

    public void forCreateComment(Comment source, List<Booking> completedBookingsOfAuthor) {
        if (source.getAuthor() == null) {
            throwNotFoundException("автор отзыва не определен");
        }

        if (source.getItem() == null) {
            throwNotFoundException("вещь для отзыва не определена");
        }

        for (Booking completedAuthorBooking : completedBookingsOfAuthor) {
            if (completedAuthorBooking.getItem().getId().equals(source.getItem().getId())) {
                return;
            }
        }

        throwException("вещь не бронировалась");
    }
}
