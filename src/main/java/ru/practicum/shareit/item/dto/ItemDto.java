package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    protected Long id;

    @NotBlank(message = "Не определено наименование")
    private String name;

    @NotBlank(message = "Не задано описание вещи")
    private String description;

    @NotNull(message = "Не определна доступность")
    private Boolean available;

    private UserDto owner;
    private Long requestId;

    private List<CommentDto> comments;

}
