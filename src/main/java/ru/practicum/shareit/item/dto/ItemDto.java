package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.AbstractDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto extends AbstractDto {

    @NotBlank(message = "Не определено наименование")
    private String name;

    @NotBlank(message = "Не задано описание")
    private String description;

    @NotNull(message = "Не определна доступность")
    private Boolean available;

    private UserDto owner;
    private ItemRequestDto itemRequest;

    private List<CommentDto> comments;

}
