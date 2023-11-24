package ru.practicum.shareit.request.contexts;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.core.contexts.SharerUserContext;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@Data
@SuperBuilder
public class CreateItemRequestContext extends SharerUserContext {
    private ItemRequestDto itemRequestDto;
}
