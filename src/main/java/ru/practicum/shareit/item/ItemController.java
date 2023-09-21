package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemExtraDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.contexts.*;
import ru.practicum.shareit.item.service.ControllerItemService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@Slf4j
@RestController
@Validated
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ControllerItemService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto create(@Valid @RequestBody ItemDto itemDto,
                          @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("добавление записи");

        CreateItemContext request = CreateItemContext.builder()
                .itemDto(itemDto)
                .sharerUserId(userId)
                .build();

        return service.create(request);
    }

    @GetMapping("/{itemId}")
    public ItemExtraDto retrieve(@PathVariable Long itemId,
                                 @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("Получение записи об отдельном бронировании");

        BasicItemContext request = BasicItemContext.builder()
                .targetItemId(itemId)
                .sharerUserId(userId)
                .build();

        return service.retrieve(request);
    }

    @GetMapping
    public List<ItemExtraDto> retrieveForOwner(@RequestParam(defaultValue = "0") @Min(0) Integer from,
                                               @RequestParam(defaultValue = "15") @Min(0) Integer size,
                                               @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("получение записи о вещах по владельцу");

        RetrieveItemForOwnerContext request = RetrieveItemForOwnerContext.builder()
                .sharerUserId(userId)
                .from(from)
                .size(size)
                .owner(userId)
                .build();

        return service.retrieveForOwner(request);
    }

    @GetMapping("/search")
    public List<ItemDto> retrieveAvailableForText(@RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                  @RequestParam(defaultValue = "15") @Min(0) Integer size,
                                                  @RequestParam String text,
                                                  @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("поиск вещей по тексту в наименовании или описании");

        RetrieveAvailableForSearchTextContext request = RetrieveAvailableForSearchTextContext.builder()
                .searchText(text)
                .from(from)
                .size(size)
                .sharerUserId(userId)
                .build();

        return service.retrieveAvailableForSearchText(request);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody(required = false) ItemDto source,
                          @PathVariable("itemId") Long itemId,
                          @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("запрос на обновление (редактирование) записи");

        UpdateItemContext request = UpdateItemContext.builder()
                .itemDto(source)
                .targetItemId(itemId)
                .sharerUserId(userId)
                .build();

        //todo перенести в сервисный слой логику source.setId(id);

        return service.update(request);
    }

    @DeleteMapping("/{itemId}")
    public void delete(@PathVariable Long itemId,
                       @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("запрос на удаление записи");

        BasicItemContext request = BasicItemContext.builder()
                .sharerUserId(userId)
                .targetItemId(itemId)
                .build();

        service.delete(request);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@Valid @RequestBody CommentDto commentDto,
                                    @PathVariable Long itemId,
                                    @RequestHeader(
                                            name = "X-Sharer-User-Id"
                                    ) Long userId) {
        log.info("запрос на добавление комментария");

        CreateCommentContext request = CreateCommentContext.builder()
                .sharerUserId(userId)
                .targetItemId(itemId)
                .comment(commentDto)
                .build();

        return service.createComment(request);
    }

}
