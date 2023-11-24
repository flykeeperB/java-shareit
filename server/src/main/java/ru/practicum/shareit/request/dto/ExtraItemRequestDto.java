package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExtraItemRequestDto extends ItemRequestDto {

    private List<ItemDto> items;
}
