package ru.practicum.shareit.booking;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.BasicValidator;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class BookingValidator extends BasicValidator<Booking> {

    @Override
    protected void validateBeforeSave(Booking source, Optional<Long> userId) {
        super.validateBeforeSave(source, userId);

        if (source.getStart() == null) {
            throwException("не задана дата начала бронирования");
        }

        if (source.getEnd() == null) {
            throwException("не задана дата окончания бронирования");
        }

        if (source.getStart().isAfter(source.getEnd())) {
            throwException("неправильные даты начала и окончания бронирования " +
                    "- даты окончания больше даты бронирования");
        }

        if (source.getStart().equals(source.getEnd())) {
            throwException("неправильные даты начала и окончания бронирования - даты не должны быть равны");
        }

        if (source.getStart().isBefore(LocalDateTime.now())) {
            throwException("неправильная дата начала бронирования - дата не должна быть раньше текущей");
        }

        if (source.getItem() == null) {
            throwNotFoundException("вещь не указана или неверный идентификатор вещи");
        }

        if (!source.getItem().getAvailable()) {
            throwException("вещь недоступна для бронирования");
        }

        if (userId.get().equals(source.getItem().getOwner().getId())) {
            throwNotFoundException("бессмысленное бронирование вещи её владельцем");
        }

        if (source.getBooker() == null) {
            throwException("не указан бронирующий вещь или неверный идентификатор бронирующего");
        }
    }

    @Override
    public void forCreate(Booking source, Optional<Long> userId) {
        super.forCreate(source, userId);
    }

    public void forApprove(Booking source, Optional<Long> userId, Boolean isApproved) {
        validateUserId(userId);

        if (!userId.get().equals(source.getItem().getOwner().getId())) {
            throwNotFoundException("не владелец не может подтердить бронь");
        }

        if (source.getStatus().equals(BookingStatus.APPROVED) && isApproved) {
            throwException("бронь уже была одобрена");
        }
    }

    public void forRetrieve(Booking booking, Optional<Long> userId) {
        if (userId.isPresent()) {
            Long sipmleUserId = userId.get();

            if (!sipmleUserId.equals(booking.getBooker().getId()) &&
                    !sipmleUserId.equals(booking.getItem().getOwner().getId())) {
                throwNotFoundException("сведения о бронировании может получить только владелец вещи либо забронировавший её");
            }
        }
    }

    public void userValidator(User user) {
        if (user == null) {
            throwNotFoundException("пользователь на найден");
        }
    }
}
