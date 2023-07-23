package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.ShareItRepository;
import ru.practicum.shareit.user.model.User;

@Repository
public interface UserRepository extends ShareItRepository<User> {
}
