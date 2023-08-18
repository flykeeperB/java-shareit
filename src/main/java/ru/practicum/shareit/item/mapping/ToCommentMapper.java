package ru.practicum.shareit.item.mapping;

import ru.practicum.shareit.item.contexts.CreateCommentContext;
import ru.practicum.shareit.item.model.Comment;

public interface ToCommentMapper {

    Comment map(CreateCommentContext context);

}
