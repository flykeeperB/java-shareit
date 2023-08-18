package ru.practicum.shareit.item.mapping.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.contexts.CreateCommentContext;
import ru.practicum.shareit.item.mapping.ToCommentMapper;
import ru.practicum.shareit.item.model.Comment;

@Component
public class ToCommentMapperImpl implements ToCommentMapper {
    @Override
    public Comment map(CreateCommentContext context) {
        Comment target = new Comment();

        target.setText(context.getComment().getText());
        target.setItem(context.getItem());
        target.setAuthor(context.getSharerUser());

        target.setId(context.getComment().getId());
        return target;
    }
}
