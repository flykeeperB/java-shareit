package ru.practicum.shareit.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.AbstractService;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.UserValidator;
import ru.practicum.shareit.user.model.User;

@Service
public class UserServiceImpl
        extends AbstractService<User, UserValidator>
        implements UserService {

    @Autowired
    public UserServiceImpl(UserRepository repository, UserValidator validator) {
        super(repository, validator);
    }

    @Override
    protected User patch(User source, User target) {
        User result = super.patch(source, new User(target));

        if (source.getName() != null) {
            result.setName(source.getName());
        }

        if (source.getEmail() != null) {
            result.setEmail(source.getEmail());
        }

        return result;
    }

    @Override
    public String getName() {
        return "Users";
    }

}
