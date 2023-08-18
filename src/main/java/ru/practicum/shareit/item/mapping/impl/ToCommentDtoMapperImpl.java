package ru.practicum.shareit.item.mapping.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.mapping.ToCommentDtoMapper;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ToCommentDtoMapperImpl implements ToCommentDtoMapper {

    @Override
    public CommentDto map(Comment source) {
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
    public List<CommentDto> map(List<Comment> source) {
        return source
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

}
