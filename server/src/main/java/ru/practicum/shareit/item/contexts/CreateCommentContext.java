package ru.practicum.shareit.item.contexts;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.item.dto.CommentDto;

@Data
@SuperBuilder
public class CreateCommentContext extends BasicItemContext {
    private final CommentDto comment;
}
