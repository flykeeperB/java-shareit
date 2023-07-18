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

    UserMapper userMapper;

    @Override
    public ItemRequestDto toDto(ItemRequest source) {
        if (source == null) {
            return null;
        }

        ItemRequestDto result = ItemRequestDto.builder()
                .description(source.getDescription())
                .requestor(userMapper.toDto(source.getRequestor()))
                .created(userMapper.toDto(source.getCreated()))
                .build();

        result.setId(source.getId());

        return result;
    }

    @Override
    public ItemRequest fromDto(ItemRequestDto source) {
        if (source == null) {
            return null;
        }

        ItemRequest result = ItemRequest.builder()
                .description(source.getDescription())
                .requestor(userMapper.fromDto(source.getRequestor()))
                .created(userMapper.fromDto(source.getCreated()))
                .build();

        result.setId(source.getId());

        return result;
    }
}
