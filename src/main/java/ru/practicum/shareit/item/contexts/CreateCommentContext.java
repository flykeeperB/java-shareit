package ru.practicum.shareit.item.contexts;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Data
@SuperBuilder
public class CreateCommentContext extends BasicItemContext {
    private final CommentDto comment;
}
