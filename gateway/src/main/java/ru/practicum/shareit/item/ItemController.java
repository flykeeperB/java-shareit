package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Slf4j
@RestController
@Validated
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody ItemDto itemDto,
                                         @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("добавление записи");

        return itemClient.create(userId, itemDto);

    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> retrieve(@PathVariable Long itemId,
                                           @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("Получение записи об отдельном бронировании");

        return itemClient.retieve(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> retrieveForOwner(@RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                   @RequestParam(defaultValue = "15") @Min(1) Integer size,
                                                   @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("получение записи о вещах по владельцу");

        return itemClient.retieve(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> retrieveAvailableForSearchText(@RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                                 @RequestParam(defaultValue = "15") @Min(1) Integer size,
                                                                 @RequestParam String text,
                                                                 @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("поиск вещей по тексту в наименовании или описании");

        return itemClient.retieve(userId, text, from, size);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestBody(required = false) ItemDto itemDto,
                                         @PathVariable("itemId") Long itemId,
                                         @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("запрос на обновление (редактирование) записи");

        return itemClient.update(userId, itemId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentDto commentDto,
                                                @PathVariable Long itemId,
                                                @RequestHeader(
                                                        name = "X-Sharer-User-Id"
                                                ) Long userId) {
        log.info("запрос на добавление комментария");

        return itemClient.createComment(userId, itemId, commentDto);
    }

}
