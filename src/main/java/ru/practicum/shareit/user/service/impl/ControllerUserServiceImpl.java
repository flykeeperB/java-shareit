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
import ru.practicum.shareit.user.contexts.CreateUserContext;
import ru.practicum.shareit.user.contexts.DeleteUserContext;
import ru.practicum.shareit.user.contexts.RetrieveUserContext;
import ru.practicum.shareit.user.contexts.UpdateUserContext;
import ru.practicum.shareit.user.service.ControllerUserService;
import ru.practicum.shareit.user.service.ExternalUserService;
import ru.practicum.shareit.user.validators.UserNullityValidator;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ControllerUserServiceImpl implements ControllerUserService, ExternalUserService {

    private final UserRepository repository;

    private final ToUserDtoMapper toUserDtoMapper;
    private final ToUserMapper toUserMapper;
    private final ToUserDtoListMapper toUserDtoListMapper;

    private final UserNullityValidator userNullityValidator;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public UserDto create(CreateUserContext context) {
        log.info("создание записи");

        User user = toUserMapper.map(context.getUserDto());

        return toUserDtoMapper.map(repository.save(user));
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public UserDto retrieve(RetrieveUserContext request) {
        log.info("получение записи по идентификатору");

        User user = retrieve(request.getTargetUserId());

        return toUserDtoMapper.map(user);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<UserDto> retrieve() {
        log.info("получение записей");

        return toUserDtoListMapper.map(repository.findAll());
    }

    //todo переписать
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<UserDto> retrieve(List<Long> ids, Optional<Long> userId) {
        log.info("получение записей по набору идентификаторов");

        return toUserDtoListMapper.map(repository.findAllById(ids));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public UserDto update(UpdateUserContext request) {
        log.info("обновление записи");

        User target = retrieve(request.getTargetUserId());

        if (request.getUserDto() == null) {
            return toUserDtoMapper.map(retrieve(request.getTargetUserId()));
        }

        User source = toUserMapper.map(request.getUserDto());

        patch(source, target);

        return toUserDtoMapper.map(repository.save(target));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(DeleteUserContext request) {
        log.info("удаление записи");

        repository.deleteById(request.getTargetUserId());
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
