package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateItemRequestDto {
    @NotBlank(message = "Не указано описание запроса о предоставлении вещи")
    private String description;
}
