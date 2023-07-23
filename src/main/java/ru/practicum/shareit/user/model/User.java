package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.AbstractModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends AbstractModel {

    @Column
    private String name;

    @Column(length = 512, unique = true)
    private String email;

    public User(User user) {
        super(user);
        this.name = user.getName();
        this.email = user.getEmail();
    }
}
