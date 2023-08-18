package ru.practicum.shareit.item.contexts;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.item.model.Item;

@Data
@SuperBuilder
public class UpdateItemContext extends CreateItemContext {
    private Item oldItem;
}
