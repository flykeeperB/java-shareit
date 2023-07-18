package ru.practicum.shareit.user.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.AbstractController;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserControllerImpl extends AbstractController<UserDto, User> {

    @Autowired
    public UserControllerImpl(UserService service, UserMapper mapper) {
        super(service, mapper);
    }

    @PostMapping
    @Override
    public UserDto create(@Valid @RequestBody UserDto source,
                          @RequestHeader("X-Sharer-User-Id") Optional<Long> userId) {
        return super.create(source, userId);
    }

    @GetMapping("/{userId}")
    @Override
    public UserDto retrieve(@PathVariable("userId") Long id,
                            @RequestHeader("X-Sharer-User-Id") Optional<Long> userId) {
        return super.retrieve(id, userId);
    }

    @GetMapping
    @Override
    public List<UserDto> retrieve(@RequestHeader("X-Sharer-User-Id") Optional<Long> userId) {
        return super.retrieve(userId);
    }

    @PatchMapping("/{userId}")
    @Override
    public UserDto update(@RequestBody UserDto source,
                          @PathVariable("userId") Long id,
                          @RequestHeader("X-Sharer-User-Id") Optional<Long> userId) {
        return super.update(source, id, userId);
    }

    @DeleteMapping("/{userId}")
    @Override
    public void delete(@PathVariable("userId") Long id,
                       @RequestHeader("X-Sharer-User-Id") Optional<Long> userId) {
        super.delete(id, userId);
    }

}
