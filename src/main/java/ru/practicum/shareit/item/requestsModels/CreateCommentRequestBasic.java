package ru.practicum.shareit.item.requestsModels;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.item.dto.CommentDto;

@Data
@SuperBuilder
public class CreateCommentRequestBasic extends BasicItemRequest {
    private final CommentDto comment;
}
