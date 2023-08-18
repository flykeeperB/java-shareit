package ru.practicum.shareit.item.mapping;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface ToCommentDtoMapper {

    CommentDto map (Comment comment);

    List<CommentDto> map(List<Comment> source);
}
