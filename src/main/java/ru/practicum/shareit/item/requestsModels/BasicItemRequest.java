package ru.practicum.shareit.item.requestsModels;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class BasicItemRequest {
    private final Long sharerUserId;
    private final Long targetItemId;
}
