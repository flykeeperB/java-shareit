package ru.practicum.shareit.item.requestsModels;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;

@Data
@Builder
public class CreateItemRequest {
    private final Long sharerUserId;

    private final ItemDto itemDto;
}
