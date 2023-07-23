package ru.practicum.shareit.item.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.AbstractController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemExtraDto;
import ru.practicum.shareit.item.mapping.CommentMapper;
import ru.practicum.shareit.item.mapping.ItemExtraMapper;
import ru.practicum.shareit.item.mapping.ItemMapper;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/items")
public class ItemControllerImpl extends AbstractController<ItemDto, Item> {

    private final ItemService service;
    private final ItemMapper mapper;
    private final UserService userService;
    private final UserMapper userMapper;
    private final ItemExtraMapper itemExtraMapper;
    private final CommentMapper commentMapper;


    @Autowired
    public ItemControllerImpl(ItemService service,
                              ItemMapper mapper,
                              UserService userService,
                              UserMapper userMapper,
                              ItemExtraMapper itemExtraMapper,
                              CommentMapper commentMapper) {
        super(service, mapper);
        this.service = service;
        this.mapper = mapper;
        this.userService = userService;
        this.userMapper = userMapper;
        this.itemExtraMapper = itemExtraMapper;
        this.commentMapper = commentMapper;
    }

    @PostMapping
    @Override
    public ItemDto create(@Valid @RequestBody ItemDto source,
                          @RequestHeader("X-Sharer-User-Id") Optional<Long> userId) {
        return super.create(source, userId);
    }

    @GetMapping("/{itemId}")
    @Override
    public ItemExtraDto retrieve(@PathVariable Long itemId,
                                 @RequestHeader("X-Sharer-User-Id") Optional<Long> userId) {
        logInfo("Получение записи об отдельном бронировании");

        Item item = service.retrieve(itemId, userId);
        ItemExtraDto result = new ItemExtraDto();

        if (userId.isPresent()) {
            if (userId.get().equals(item.getOwner().getId())) {
                return itemExtraMapper.toDto(item);
            }
        }

        mapper.toDto(item, result);

        return result;
    }

    @Override
    public List<ItemDto> retrieve(@RequestHeader("X-Sharer-User-Id") Optional<Long> userId) {
        return super.retrieve(userId);
    }

    @GetMapping
    public List<ItemExtraDto> retrieveForOwner(@RequestHeader("X-Sharer-User-Id") Optional<Long> userId) {
        logInfo("получение записи о вещах по владельцу");

        return itemExtraMapper.toDto(service.retrieveForOwner(userId));
    }

    @GetMapping("/search")
    public List<ItemDto> retrieveAvailableForText(@RequestParam String text,
                                                  @RequestHeader("X-Sharer-User-Id") Optional<Long> userId) {
        logInfo("поиск вещей по тексту в наименовании или описании");

        return mapper.toDto(service.retrieveAvailableForSearchText(text, userId));
    }

    @PatchMapping("/{itemId}")
    @Override
    public ItemDto update(@RequestBody(required = false) ItemDto source,
                          @PathVariable("itemId") Long id,
                          @RequestHeader("X-Sharer-User-Id") Optional<Long> userId) {

        return super.update(source, id, userId);
    }

    @Override
    public String getName() {
        return "Items";
    }

    @DeleteMapping("/{itemId}")
    @Override
    public void delete(@PathVariable Long itemId,
                       @RequestHeader("X-Sharer-User-Id") Optional<Long> userId) {
        super.delete(itemId, userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@Valid @RequestBody CommentDto source,
                                    @PathVariable Long itemId,
                                    @RequestHeader("X-Sharer-User-Id") Optional<Long> userId) {
        return commentMapper.toDto(service.createComment(
                commentMapper.fromDto(source),
                itemId,
                userId));
    }
}
