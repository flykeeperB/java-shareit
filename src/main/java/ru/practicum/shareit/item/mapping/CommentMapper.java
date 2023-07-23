package ru.practicum.shareit.item.mapping;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.AbstractMapper;
import ru.practicum.shareit.Mapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

@Component
public class CommentMapper
        extends AbstractMapper<Comment, CommentDto>
        implements Mapper<Comment, CommentDto> {

    @Override
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

    @Override
    public CommentDto toDto(Comment source) {
        return toDto(source, new CommentDto());
    }

    @Override
    public Comment fromDto(CommentDto source, Comment target) {
        if (source == null) {
            return null;
        }

        target.setText(source.getText());

        return target;
    }

    @Override
    public Comment fromDto(CommentDto source) {
        return fromDto(source, new Comment());
    }
}
