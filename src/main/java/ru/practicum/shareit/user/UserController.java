package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.service.ControllerUserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.contexts.BasicUserContext;
import ru.practicum.shareit.user.contexts.DeleteUserContext;
import ru.practicum.shareit.user.contexts.RetrieveUserContext;
import ru.practicum.shareit.user.contexts.UpdateUserContext;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final ControllerUserService service;

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto source) {
        log.info("запрос создания записи");

        BasicUserContext basicUserContext = BasicUserContext.builder()
                .userDto(source)
                .build();

        return service.create(basicUserContext);
    }

    @GetMapping("/{userId}")
    public UserDto retrieve(@PathVariable("userId") Long userId) {
        log.info("запрос на получение записи по идентификатору");

        RetrieveUserContext retrieveUserContext = RetrieveUserContext.builder()
                .targetUserId(userId)
                .build();

        return service.retrieve(retrieveUserContext);
    }

    @GetMapping
    public List<UserDto> retrieve() {
        log.info("запрос на получение записей обо всех пользователях");

        return service.retrieve();
    }

    @PatchMapping("/{userId}")
    public UserDto update(@RequestBody UserDto userDto,
                          @PathVariable("userId") Long userId) {
        log.info("запрос на обновление (редактирование) записи");

        UpdateUserContext updateUserContext = UpdateUserContext.builder()
                .targetUserId(userId)
                .userDto(userDto)
                .build();

        return service.update(updateUserContext);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable("userId") Long userId) {
        log.info("запрос на удаление записи");

        DeleteUserContext deleteUserContext = DeleteUserContext.builder()
                .targetUserId(userId)
                .build();

        service.delete(deleteUserContext);
    }

}
