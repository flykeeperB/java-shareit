package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.AbstractDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenericBookingDto extends AbstractDto {

    private Long bookerId;

    @NotNull(message = "Не определена дата начала бронирования")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    protected LocalDateTime start;

    @NotNull(message = "Не определена дата окончания бронирования")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    protected LocalDateTime end;
}
