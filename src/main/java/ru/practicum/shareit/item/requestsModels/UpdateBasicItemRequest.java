package ru.practicum.shareit.item.requestsModels;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.item.dto.ItemDto;

@Data
@SuperBuilder
public class UpdateBasicItemRequest extends BasicItemRequest {
    private final ItemDto itemDto;
}
