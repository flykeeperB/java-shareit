package ru.practicum.shareit.user.impl;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.AbstractService;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.model.User;

@Service
public class UserServiceImpl
        extends AbstractService<User>
        implements UserService {

    public UserServiceImpl(UserRepository repository) {
        super(repository, "Users");
    }

}
