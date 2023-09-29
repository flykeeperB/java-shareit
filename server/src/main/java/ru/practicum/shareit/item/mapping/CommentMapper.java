package ru.practicum.shareit.item.mapping;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface CommentMapper {

    CommentDto mapToCommentDto(Comment comment);

    List<CommentDto> mapToCommentDto(List<Comment> source);

    Comment mapToComment(CommentDto commentDto, User sharerUser, Item item);

}
