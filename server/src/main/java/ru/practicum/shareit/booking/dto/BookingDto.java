package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    protected Long id;
    private Long bookerId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    protected LocalDateTime start;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    protected LocalDateTime end;
}
