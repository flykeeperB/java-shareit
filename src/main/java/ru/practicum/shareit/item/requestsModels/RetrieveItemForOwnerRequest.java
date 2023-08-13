package ru.practicum.shareit.item.requestsModels;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RetrieveItemForOwnerRequest {
    private final Long sharerUserId;
    private final Long owner;
}
