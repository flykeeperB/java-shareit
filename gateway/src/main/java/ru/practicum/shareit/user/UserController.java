package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.validators.UserNotBlankNameValidator;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserClient userClient;

    private final UserNotBlankNameValidator userNotBlankNameValidator;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody UserDto userDto) {
        log.info("запрос создания записи");

        userNotBlankNameValidator.validate(userDto);

        return userClient.create(userDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> retrieve(@PathVariable("userId") Long userId) {
        log.info("запрос на получение записи по идентификатору");

        return userClient.retieve(userId);
    }

    @GetMapping
    public ResponseEntity<Object> retrieve() {
        log.info("запрос на получение записей обо всех пользователях");

        return userClient.retieve();
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@RequestBody UserDto userDto,
                                         @PathVariable("userId") Long userId) {
        log.info("запрос на обновление (редактирование) записи");

        return userClient.update(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable("userId") Long userId) {
        log.info("запрос на удаление записи");

        userClient.delete(userId);
    }

}
