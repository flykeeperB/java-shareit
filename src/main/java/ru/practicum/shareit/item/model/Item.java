package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.AbstractModel;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Builder
@Data
@AllArgsConstructor
public class Item extends AbstractModel {

    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private ItemRequest itemRequest;

    public Item(Item source) {
        super(source);
        this.description = source.getDescription();
        this.name = source.getName();
        this.itemRequest = source.getItemRequest();
        this.owner = source.getOwner();
        this.available = source.getAvailable();
    }
}
