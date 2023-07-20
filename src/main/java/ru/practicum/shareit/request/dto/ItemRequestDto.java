package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.AbstractDto;
import ru.practicum.shareit.user.dto.UserDto;

@Builder
@Data
@AllArgsConstructor
public class ItemRequestDto extends AbstractDto {
    private String description;
    private UserDto requestor;
    private UserDto created;
}
