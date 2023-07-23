package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.AbstractDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto extends AbstractDto {

    @NotBlank(message = "Пустой комментарий")
    private String text;

    private String authorName;
    private LocalDateTime created;
}
