package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {

    protected Long id;

    private Long bookerId;

    @NotNull(message = "Не определена дата начала бронирования")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    protected LocalDateTime start;

    @NotNull(message = "Не определена дата окончания бронирования")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    protected LocalDateTime end;
}
