package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.contexts.BasicUserContext;

import ru.practicum.shareit.user.contexts.DeleteUserContext;
import ru.practicum.shareit.user.contexts.RetrieveUserContext;
import ru.practicum.shareit.user.contexts.UpdateUserContext;
import ru.practicum.shareit.user.mapping.UserMapper;
import ru.practicum.shareit.user.mapping.impl.UserMapperImpl;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.impl.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.validators.UserNullityValidator;
import ru.practicum.shareit.user.validators.impl.UserNullityValidatorImpl;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    private UserMapper userMapper;

    private UserNullityValidator userNullityValidator;

    private User testSourceUser;
    private UserDto testSourceUserDto;

    @BeforeEach
    public void setUp() {

        userMapper = new UserMapperImpl();

        userNullityValidator = new UserNullityValidatorImpl();

        userService = new UserServiceImpl(userRepository,
                userMapper,
                userNullityValidator
        );

        testSourceUser = new User(1L, "alice", "alice@yandex.ru");
        testSourceUserDto = new UserDto(1L, "alice", "alice@yandex.ru");
    }

    @Test
    public void createTest() {
        when(userRepository.save(any(User.class))).thenReturn(testSourceUser);

        BasicUserContext testContext = BasicUserContext.builder()
                .userDto(testSourceUserDto)
                .build();

        UserDto testResultUser = userService.create(testContext);

        assertNotNull(testResultUser, "Не возвращается результат создания записи.");
        assertThat(testResultUser.getId(), equalTo(testSourceUser.getId()));
        assertThat(testResultUser.getName(), equalTo(testSourceUser.getName()));
        assertThat(testResultUser.getEmail(), equalTo(testSourceUser.getEmail()));
        Mockito.verify(userRepository, Mockito.times(1))
                .save(any(User.class));
    }

    @Test
    public void retrieveTest() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(testSourceUser));

        RetrieveUserContext testContext = RetrieveUserContext.builder()
                .targetUserId(1L)
                .build();

        UserDto testResultUser = userService.retrieve(testContext);

        assertNotNull(testResultUser, "Не возвращается результат создания записи.");
        assertThat(testResultUser.getId(), equalTo(testSourceUser.getId()));
        assertThat(testResultUser.getName(), equalTo(testSourceUser.getName()));
        assertThat(testResultUser.getEmail(), equalTo(testSourceUser.getEmail()));
        Mockito.verify(userRepository, Mockito.times(1))
                .findById(anyLong());
    }

    @Test
    public void retrieveAllTest() {

        when(userRepository.findAll()).thenReturn(List.of(testSourceUser));

        List<UserDto> testResultUser = userService.retrieve();

        assertNotNull(testResultUser, "Не возвращается результат создания записи.");
        assertEquals(1, testResultUser.size(), "Неверное количество записей в результате");

        UserDto oneTestResultUser = testResultUser.get(0);

        assertThat(oneTestResultUser.getId(), equalTo(testSourceUser.getId()));
        assertThat(oneTestResultUser.getName(), equalTo(testSourceUser.getName()));
        assertThat(oneTestResultUser.getEmail(), equalTo(testSourceUser.getEmail()));
        Mockito.verify(userRepository, Mockito.times(1))
                .findAll();
    }

    @Test
    public void updateTest() {

        testSourceUser.setName("updated");
        testSourceUser.setEmail("aliceUpdated@yandex.ru");
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(testSourceUser));
        when(userRepository.save(any(User.class))).thenReturn(testSourceUser);

        UpdateUserContext testContext = UpdateUserContext.builder()
                .targetUserId(1L)
                .userDto(new UserDto(1L, "updated", "aliceUpdated@yandex.ru"))
                .build();

        UserDto testResultUser = userService.update(testContext);

        assertNotNull(testResultUser, "Не возвращается результат создания записи.");
        assertThat(testResultUser.getId(), equalTo(testSourceUser.getId()));
        assertThat(testResultUser.getName(), equalTo("updated"));
        assertThat(testResultUser.getEmail(), equalTo("aliceUpdated@yandex.ru"));
        Mockito.verify(userRepository, Mockito.times(1))
                .findById(anyLong());
        Mockito.verify(userRepository, Mockito.times(1))
                .save(any(User.class));
    }

    @Test
    public void deleteTest() {

        DeleteUserContext testContext = DeleteUserContext.builder()
                .targetUserId(1L)
                .build();

        userService.delete(testContext);

        Mockito.verify(userRepository, Mockito.times(1))
                .deleteById(anyLong());
    }
}