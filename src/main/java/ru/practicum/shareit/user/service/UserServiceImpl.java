package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.mapping.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.requestsModels.CreateUserRequest;
import ru.practicum.shareit.user.requestsModels.DeleteUserRequest;
import ru.practicum.shareit.user.requestsModels.RetrieveUserRequest;
import ru.practicum.shareit.user.requestsModels.UpdateUserRequest;
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

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public UserDto create(CreateUserRequest createUserRequest) {
        log.info("создание записи");

        User user = userMapper.fromDto(createUserRequest.getUserDto());

        return userMapper.toDto(repository.save(user));
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public UserDto retrieve(RetrieveUserRequest retrieveUserRequest) {
        log.info("получение записи по идентификатору");

        User user = retrieve(retrieveUserRequest.getTargetUserId());

        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<UserDto> retrieve() {
        log.info("получение записей");

        return userMapper.toDto(repository.findAll());
    }

    //todo переписать
    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<UserDto> retrieve(List<Long> ids, Optional<Long> userId) {
        log.info("получение записей по набору идентификаторов");

        return userMapper.toDto(repository.findAllById(ids));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public UserDto update(UpdateUserRequest updateUserRequest) {
        log.info("обновление записи");

        User target = retrieve(updateUserRequest.getTargetUserId());

        if (updateUserRequest.getUserDto() == null) {
            return userMapper.toDto(retrieve(updateUserRequest.getTargetUserId()));
        }

        User source = userMapper.fromDto(updateUserRequest.getUserDto());

        patch(source, target);

        return userMapper.toDto(repository.save(target));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(DeleteUserRequest deleteUserRequest) {
        log.info("удаление записи");

        repository.deleteById(deleteUserRequest.getTargetUserId());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public User retrieve(Long id) {
        Optional<User> result = repository.findById(id);

        userNullityValidator.Validate(result);

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
