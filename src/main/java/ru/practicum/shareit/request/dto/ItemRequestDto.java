package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.AbstractDto;
import ru.practicum.shareit.user.dto.UserDto;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequestDto extends AbstractDto {
    private String description;
    private UserDto requestor;
    private UserDto created;
}
