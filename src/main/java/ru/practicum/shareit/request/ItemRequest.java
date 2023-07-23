package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.AbstractModel;
import ru.practicum.shareit.user.model.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest extends AbstractModel {
    private String description;
    private User requestor;
    private User created;
}
