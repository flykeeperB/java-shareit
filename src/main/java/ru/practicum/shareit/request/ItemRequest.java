package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemRequest {
    private Long id;
    private String description;
    private User requestor;
    private User created;
}
