package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.mapping.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.contexts.BasicUserContext;
import ru.practicum.shareit.user.contexts.DeleteUserContext;
import ru.practicum.shareit.user.contexts.RetrieveUserContext;
import ru.practicum.shareit.user.contexts.UpdateUserContext;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.validators.UserNotBlankNameValidator;
import ru.practicum.shareit.user.validators.UserNullityValidator;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    private final UserMapper userMapper;

    private final UserNullityValidator userNullityValidator;
    private final UserNotBlankNameValidator userNotBlankNameValidator;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public UserDto create(BasicUserContext context) {
        log.info("создание записи");

        userNotBlankNameValidator.validate(context);

        User user = userMapper.mapToUser(context.getUserDto());

        return userMapper.mapToUserDto(repository.save(user));
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public UserDto retrieve(RetrieveUserContext context) {
        log.info("получение записи по идентификатору");

        User user = retrieve(context.getTargetUserId());

        return userMapper.mapToUserDto(user);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<UserDto> retrieve() {
        log.info("получение записей");

        return userMapper.mapToUserDto(repository.findAll());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public UserDto update(UpdateUserContext context) {
        log.info("обновление записи");

        User target = retrieve(context.getTargetUserId());

        if (context.getUserDto() == null) {
            return userMapper.mapToUserDto(retrieve(context.getTargetUserId()));
        }

        User source = userMapper.mapToUser(context.getUserDto());

        patch(source, target);

        return userMapper.mapToUserDto(repository.save(target));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(DeleteUserContext context) {
        log.info("удаление записи");

        repository.deleteById(context.getTargetUserId());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    private User retrieve(Long id) {
        Optional<User> result = repository.findById(id);

        userNullityValidator.validate(result);

        return result.get();
    }

    private void patch(User source, User target) {

        if (source.getId() != null) {
            target.setId(source.getId());
        }

        if (source.getName() != null) {
            target.setName(source.getName());
        }

        if (source.getEmail() != null) {
            target.setEmail(source.getEmail());
        }

    }

}
