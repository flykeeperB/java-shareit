package ru.practicum.shareit.user.impl;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.InMemoryRepository;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserRepositoryImpl
        extends InMemoryRepository<User>
        implements UserRepository {

    private final Map<String, User> usersByEmail = new HashMap<>();

    public UserRepositoryImpl() {
        super("Users");
    }

    @Override
    protected void validateToSave(User user) {
        super.validateToSave(user);
        if (usersByEmail.containsKey(user.getEmail())) {
            if (!usersByEmail.get(user.getEmail()).getId().equals(user.getId())) {
                throw new AlreadyExistException("Пользователь с таким e-mail уже имеется");
            }
        }
    }

    @Override
    public User create(User source) {
        User result = super.create(source);
        usersByEmail.put(result.getEmail(), result);
        return result;
    }

    // Выполнение частичного обновления информации
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
    public User update(User source) {
        String email = retrieve(source.getId()).getEmail();

        User result = super.update(new User(source));

        usersByEmail.remove(email);

        usersByEmail.put(result.getEmail(), result);
        return result;
    }

    @Override
    public void delete(Long id) {
        String email = retrieve(id).getEmail();
        super.delete(id);
        usersByEmail.remove(email);
    }

}
