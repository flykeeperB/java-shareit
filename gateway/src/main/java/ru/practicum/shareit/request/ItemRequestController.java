package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Slf4j
@RestController
@Validated
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody ItemRequestDto itemRequestDto,
                                         @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("создание запроса о предоставлении вещи");

        return itemRequestClient.create(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> retrieve(@RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("получение списка запросов о предоставлении вещей, созданных отдельным пользователем");

        return itemRequestClient.retieve(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> retrieve(@RequestParam(defaultValue = "0") @Min(0) Integer from,
                                              @RequestParam(defaultValue = "15") @Min(1) Integer size,
                                              @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("получение общего списка запросов о преодставлении вещей (постранично)");

        return itemRequestClient.retieve(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> retrieve(@PathVariable Long requestId,
                                        @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("получение сведений о конкретном запросе о предоставлении вещи");

        return itemRequestClient.retieve(userId, requestId);
    }
}
