package ru.practicum.shareit.item.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.AbstractController;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemMapper;
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


    @Autowired
    public ItemControllerImpl(ItemService service,
                              ItemMapper mapper,
                              UserService userService,
                              UserMapper userMapper) {
        super(service, mapper, "Items");
        this.service = service;
        this.mapper = mapper;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    private void validateUserId(Optional<Long> userId) {
        if (userId.isEmpty()) {
            throw new ValidationException("Не задан идентификатор пользователя");
        }
    }

    private void validateOwner(Long userId, Long itemId) {
        if (!getService().retrieve(itemId).getOwner().getId().equals(userId)) {
            throw new AccessDeniedException("Изменять запись может только её владелец");
        }
    }

    @PostMapping
    @Override
    public ItemDto create(@Valid @RequestBody ItemDto source,
                          @RequestHeader("X-Sharer-User-Id") Optional<Long> userId) {
        validateUserId(userId);
        source.setOwner(userMapper.toDto(userService.retrieve(userId.get())));

        return super.create(source, userId);
    }

    @GetMapping("/{itemId}")
    @Override
    public ItemDto retrieve(@PathVariable Long itemId,
                            @RequestHeader("X-Sharer-User-Id") Optional<Long> userId) {
        return super.retrieve(itemId, userId);
    }

    @Override
    public List<ItemDto> retrieve(@RequestHeader("X-Sharer-User-Id") Optional<Long> userId) {
        return super.retrieve(userId);
    }

    @GetMapping
    public List<ItemDto> retrieveForOwner(@RequestHeader("X-Sharer-User-Id") Optional<Long> userId) {
        logInfo("получение записи о вещах по владельцу");

        validateUserId(userId);

        return mapper.toDto(service.retrieveForOwner(userId.get()));
    }

    @GetMapping("/search")
    public List<ItemDto> retrieveAvailableForText(@RequestParam String text,
                                                  @RequestHeader("X-Sharer-User-Id") Optional<Long> userId) {
        logInfo("поиск вещей по тексту в наименовании или описании");

        validateUserId(userId);

        return mapper.toDto(service.retrieveAvailableForSearchText(text));
    }

    @PatchMapping("/{itemId}")
    @Override
    public ItemDto update(@RequestBody(required = false) ItemDto source,
                          @PathVariable("itemId") Long id,
                          @RequestHeader("X-Sharer-User-Id") Optional<Long> userId) {
        validateUserId(userId);
        validateOwner(userId.get(), id);

        return super.update(source, id, userId);
    }

    @DeleteMapping("/{itemId}")
    @Override
    public void delete(@PathVariable Long itemId,
                       @RequestHeader("X-Sharer-User-Id") Optional<Long> userId) {
        super.delete(itemId, userId);
    }
}
