package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.mapping.UserMapper;

@Component
@AllArgsConstructor
public class ItemRequestMapper {

    private final UserMapper userMapper;

    public ItemRequestDto toDto(ItemRequest source, ItemRequestDto target) {
        if (source == null) {
            return null;
        }

        target.setDescription(source.getDescription());
        target.setRequestor(userMapper.toDto(source.getRequestor()));
        target.setCreated(userMapper.toDto(source.getCreated()));

        target.setId(source.getId());

        return target;
    }

    public ItemRequest fromDto(ItemRequestDto source, ItemRequest target) {
        if (source == null) {
            return null;
        }

        target.setDescription(source.getDescription());
        target.setRequestor(userMapper.fromDto(source.getRequestor()));
        target.setCreated(userMapper.fromDto(source.getCreated()));

        target.setId(source.getId());

        return target;
    }

    public ItemRequestDto toDto(ItemRequest source) {
        return toDto(source, new ItemRequestDto());
    }

    public ItemRequest fromDto(ItemRequestDto source) {
        return fromDto(source, new ItemRequest());
    }
}
