package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.AbstractModel;
import ru.practicum.shareit.user.model.User;

@Builder
@Data
@AllArgsConstructor
public class ItemRequest extends AbstractModel {
    private String description;
    private User requestor;
    private User created;

    public ItemRequest(ItemRequest source) {
        super(source);

        this.description = source.getDescription();
        this.created = source.getCreated();
        this.requestor = source.getRequestor();
    }
}
