package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.requestsModels.CreateUserRequest;
import ru.practicum.shareit.user.requestsModels.DeleteUserRequest;
import ru.practicum.shareit.user.requestsModels.RetrieveUserRequest;
import ru.practicum.shareit.user.requestsModels.UpdateUserRequest;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto source) {
        log.info("запрос создания записи");

        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                .userDto(source)
                .build();

        return service.create(createUserRequest);
    }

    @GetMapping("/{userId}")
    public UserDto retrieve(@PathVariable("userId") Long userId) {
        log.info("запрос на получение записи по идентификатору");

        RetrieveUserRequest retrieveUserRequest = RetrieveUserRequest.builder()
                .targetUserId(userId)
                .build();

        return service.retrieve(retrieveUserRequest);
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

        UpdateUserRequest updateUserRequest = UpdateUserRequest.builder()
                .targetUserId(userId)
                .userDto(userDto)
                .build();

        //todo переписать или нет source.setId(id);
        return service.update(updateUserRequest);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable("userId") Long userId) {
        log.info("запрос на удаление записи");

        DeleteUserRequest deleteUserRequest = DeleteUserRequest.builder()
                .targetUserId(userId)
                .build();

        service.delete(deleteUserRequest);
    }

}
