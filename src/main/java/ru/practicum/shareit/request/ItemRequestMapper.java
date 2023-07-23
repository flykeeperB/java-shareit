package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.AbstractMapper;
import ru.practicum.shareit.Mapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserMapper;

@Component
@AllArgsConstructor
public class ItemRequestMapper
        extends AbstractMapper<ItemRequest, ItemRequestDto>
        implements Mapper<ItemRequest, ItemRequestDto> {

    private final UserMapper userMapper;

    @Override
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

    @Override
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

    @Override
    public ItemRequestDto toDto(ItemRequest source) {
        return toDto(source, new ItemRequestDto());
    }

    @Override
    public ItemRequest fromDto(ItemRequestDto source) {
        return fromDto(source, new ItemRequest());
    }
}
