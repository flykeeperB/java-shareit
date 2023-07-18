package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.AbstractDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
public class ItemDto extends AbstractDto {
    @NotBlank(message = "Не определено наименование")
    private String name;

    @NotBlank(message = "Не задано описание")
    private String description;

    @NotNull(message = "Не определна доступность")
    private Boolean available;

    private UserDto owner;
    private ItemRequestDto itemRequestDto;
}
