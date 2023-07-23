package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.AbstractMapper;
import ru.practicum.shareit.Mapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Component
public class UserMapper
        extends AbstractMapper<User, UserDto>
        implements Mapper<User, UserDto> {

    @Override
    public UserDto toDto(User source, UserDto target) {
        if (source == null) {
            return null;
        }

        target.setName(source.getName());
        target.setEmail(source.getEmail());
        target.setId(source.getId());

        return target;
    }

    @Override
    public User fromDto(UserDto source, User target) {
        if (source == null) {
            return null;
        }

        target.setName(source.getName());
        target.setEmail(source.getEmail());
        target.setId(source.getId());

        return target;
    }

    @Override
    public UserDto toDto(User source) {
        return toDto(source, new UserDto());
    }

    @Override
    public User fromDto(UserDto source) {
        return fromDto(source, new User());
    }
}
