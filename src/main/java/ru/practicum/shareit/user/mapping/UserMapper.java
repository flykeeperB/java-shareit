package ru.practicum.shareit.user.mapping;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDto toDto(User source, UserDto target) {
        if (source == null) {
            return null;
        }

        target.setName(source.getName());
        target.setEmail(source.getEmail());
        target.setId(source.getId());

        return target;
    }

    public User fromDto(UserDto source, User target) {
        if (source == null) {
            return null;
        }

        target.setName(source.getName());
        target.setEmail(source.getEmail());
        target.setId(source.getId());

        return target;
    }

    public UserDto toDto(User source) {
        return toDto(source, new UserDto());
    }

    public User fromDto(UserDto source) {
        return fromDto(source, new User());
    }

    public List<UserDto> toDto(List<User> source) {
        return source
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
