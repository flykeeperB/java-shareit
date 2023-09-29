package ru.practicum.shareit.request.contexts;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.core.contexts.SharerUserContext;

@Data
@SuperBuilder
public class RetrieveItemRequestsForUserContext extends SharerUserContext {
}
