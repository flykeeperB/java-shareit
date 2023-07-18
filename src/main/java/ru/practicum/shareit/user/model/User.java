package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.AbstractModel;

@Builder
@Data
@AllArgsConstructor
public class User extends AbstractModel {

    private String name;
    private String email;

    public User(User user) {
        super(user);
        this.name = user.getName();
        this.email = user.getEmail();
    }
}
