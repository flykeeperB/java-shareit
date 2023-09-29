package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    protected Long id;

    @NotBlank(message = "Пустой комментарий")
    private String text;

    private String authorName;
    private LocalDateTime created;
}
