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
    public UserDto toDto(User source) {
        if (source == null) {
            return null;
        }

        UserDto result = UserDto.builder()
                .name(source.getName())
                .email(source.getEmail())
                .build();

        result.setId(source.getId());

        return result;
    }

    @Override
    public User fromDto(UserDto source) {
        if (source == null) {
            return null;
        }

        User result = User.builder()
                .name(source.getName())
                .email(source.getEmail())
                .build();

        result.setId(source.getId());

        return result;
    }
}
