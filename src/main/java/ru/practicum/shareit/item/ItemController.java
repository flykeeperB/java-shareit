package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemExtraDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.requestsModels.*;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService service;

    @PostMapping
    public ItemDto create(@Valid @RequestBody ItemDto itemDto,
                          @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("добавление записи");

        CreateItemRequest request = CreateItemRequest.builder()
                .itemDto(itemDto)
                .sharerUserId(userId)
                .build();

        return service.create(request);
    }

    @GetMapping("/{itemId}")
    public ItemExtraDto retrieve(@PathVariable Long itemId,
                                 @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("Получение записи об отдельном бронировании");

        BasicItemRequest request = BasicItemRequest.builder()
                .targetItemId(itemId)
                .sharerUserId(userId)
                .build();

        return service.retrieve(request);
    }

    @GetMapping
    public List<ItemExtraDto> retrieveForOwner(@RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("получение записи о вещах по владельцу");

        RetrieveItemForOwnerRequest request = RetrieveItemForOwnerRequest.builder()
                .sharerUserId(userId)
                .owner(userId)
                .build();

        return service.retrieveForOwner(request);
    }

    @GetMapping("/search")
    public List<ItemDto> retrieveAvailableForText(@RequestParam String text,
                                                  @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("поиск вещей по тексту в наименовании или описании");

        RetrieveAvailableForSearchTextRequest request = RetrieveAvailableForSearchTextRequest.builder()
                .searchText(text)
                .sharerUserId(userId)
                .build();

        return service.retrieveAvailableForSearchText(request);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody(required = false) ItemDto source,
                          @PathVariable("itemId") Long itemId,
                          @RequestHeader(name = "X-Sharer-User-Id") Long userId) {
        log.info("запрос на обновление (редактирование) записи");

        UpdateBasicItemRequest request = UpdateBasicItemRequest.builder()
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

        BasicItemRequest request = BasicItemRequest.builder()
                .sharerUserId(userId)
                .targetItemId(itemId)
                .build();

        service.delete(request);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@Valid @RequestBody CommentDto commentDto,
                                    @PathVariable Long itemId,
                                    @RequestHeader(
                                            name = "X-Sharer-User-Id"
                                    ) Long userId) {
        log.info("запрос на добавление комментария");

        CreateCommentRequestBasic request = CreateCommentRequestBasic.builder()
                .sharerUserId(userId)
                .targetItemId(itemId)
                .comment(commentDto)
                .build();

        return service.createComment(request);
    }

}
