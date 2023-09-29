package ru.practicum.shareit.item.mapping.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.mapping.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapperImpl implements CommentMapper {

    @Override
    public CommentDto mapToCommentDto(Comment source) {
        if (source == null) {
            return null;
        }

        CommentDto target = new CommentDto();

        target.setCreated(source.getCreated());
        target.setAuthorName(source.getAuthor().getName());
        target.setText(source.getText());
        target.setId(source.getId());

        return target;
    }

    @Override
    public List<CommentDto> mapToCommentDto(List<Comment> source) {
        if (source == null) {
            return new ArrayList<>();
        }

        return source
                .stream()
                .map(this::mapToCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    public Comment mapToComment(CommentDto commentDto, User sharerUser, Item item) {
        Comment target = new Comment();

        target.setText(commentDto.getText());
        target.setItem(item);
        target.setAuthor(sharerUser);

        target.setId(commentDto.getId());
        return target;
    }
}
