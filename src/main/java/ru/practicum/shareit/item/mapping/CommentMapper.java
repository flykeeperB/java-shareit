package ru.practicum.shareit.item.mapping;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

    public CommentDto toDto(Comment source, CommentDto target) {
        if (source == null) {
            return null;
        }

        target.setCreated(source.getCreated());
        target.setAuthorName(source.getAuthor().getName());
        target.setText(source.getText());
        target.setId(source.getId());

        return target;
    }

    public CommentDto toDto(Comment source) {
        return toDto(source, new CommentDto());
    }

    public Comment fromDto(CommentDto source, Comment target) {
        if (source == null) {
            return null;
        }

        target.setText(source.getText());

        return target;
    }

    public Comment fromDto(CommentDto source) {
        return fromDto(source, new Comment());
    }

    public List<CommentDto> toDto(List<Comment> source) {
        return source
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
