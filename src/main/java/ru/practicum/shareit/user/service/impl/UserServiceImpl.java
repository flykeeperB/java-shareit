package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.mapping.ToUserDtoListMapper;
import ru.practicum.shareit.user.mapping.ToUserDtoMapper;
import ru.practicum.shareit.user.mapping.ToUserMapper;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.contexts.BasicUserContext;
import ru.practicum.shareit.user.contexts.DeleteUserContext;
import ru.practicum.shareit.user.contexts.RetrieveUserContext;
import ru.practicum.shareit.user.contexts.UpdateUserContext;
import ru.practicum.shareit.user.service.ControllerUserService;
import ru.practicum.shareit.user.service.ExternalUserService;
import ru.practicum.shareit.user.validators.UserNotBlankNameValidator;
import ru.practicum.shareit.user.validators.UserNullityValidator;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements ControllerUserService, ExternalUserService {

    private final UserRepository repository;

    private final ToUserDtoMapper toUserDtoMapper;
    private final ToUserMapper toUserMapper;
    private final ToUserDtoListMapper toUserDtoListMapper;

    private final UserNullityValidator userNullityValidator;
    private final UserNotBlankNameValidator userNotBlankNameValidator;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public UserDto create(BasicUserContext context) {
        log.info("создание записи");

        userNotBlankNameValidator.validate(context);

        User user = toUserMapper.map(context.getUserDto());

        return toUserDtoMapper.map(repository.save(user));
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public UserDto retrieve(RetrieveUserContext context) {
        log.info("получение записи по идентификатору");

        User user = retrieve(context.getTargetUserId());

        return toUserDtoMapper.map(user);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<UserDto> retrieve() {
        log.info("получение записей");

        return toUserDtoListMapper.map(repository.findAll());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public UserDto update(UpdateUserContext context) {
        log.info("обновление записи");

        User target = retrieve(context.getTargetUserId());

        if (context.getUserDto() == null) {
            return toUserDtoMapper.map(retrieve(context.getTargetUserId()));
        }

        User source = toUserMapper.map(context.getUserDto());

        patch(source, target);

        return toUserDtoMapper.map(repository.save(target));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(DeleteUserContext context) {
        log.info("удаление записи");

        repository.deleteById(context.getTargetUserId());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public User retrieve(Long id) {
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
