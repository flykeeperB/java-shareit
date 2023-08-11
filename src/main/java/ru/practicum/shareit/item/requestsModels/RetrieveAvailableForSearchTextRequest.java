package ru.practicum.shareit.item.requestsModels;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RetrieveAvailableForSearchTextRequest {
    private final String searchText;
    private final Long sharerUserId;
}
