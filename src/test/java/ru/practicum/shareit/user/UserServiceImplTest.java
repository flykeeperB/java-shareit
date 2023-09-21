package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.contexts.BasicUserContext;

import ru.practicum.shareit.user.contexts.DeleteUserContext;
import ru.practicum.shareit.user.contexts.RetrieveUserContext;
import ru.practicum.shareit.user.contexts.UpdateUserContext;
import ru.practicum.shareit.user.mapping.ToUserDtoListMapper;
import ru.practicum.shareit.user.mapping.ToUserDtoMapper;
import ru.practicum.shareit.user.mapping.ToUserMapper;
import ru.practicum.shareit.user.mapping.impl.ToUserDtoListMapperImpl;
import ru.practicum.shareit.user.mapping.impl.ToUserDtoMapperImpl;
import ru.practicum.shareit.user.mapping.impl.ToUserMapperImpl;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.impl.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.validators.UserNotBlankNameValidator;
import ru.practicum.shareit.user.validators.UserNullityValidator;
import ru.practicum.shareit.user.validators.impl.UserNotBlankNameValidatorImpl;
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

    private ToUserDtoMapper toUserDtoMapper;
    private ToUserMapper toUserMapper;
    private ToUserDtoListMapper toUserDtoListMapper;
    private UserNullityValidator userNullityValidator;
    private UserNotBlankNameValidator userNotBlankNameValidator;

    private User testSourceUser;
    private UserDto testSourceUserDto;

    @BeforeEach
    void setUp() {
        toUserDtoMapper = new ToUserDtoMapperImpl();
        toUserMapper = new ToUserMapperImpl();
        toUserDtoListMapper = new ToUserDtoListMapperImpl(toUserDtoMapper);
        userNullityValidator = new UserNullityValidatorImpl();
        userNotBlankNameValidator = new UserNotBlankNameValidatorImpl();

        userService = new UserServiceImpl(userRepository,
                toUserDtoMapper,
                toUserMapper,
                toUserDtoListMapper,
                userNullityValidator,
                userNotBlankNameValidator
        );

        testSourceUser = new User(1L, "alice", "alice@yandex.ru");
        testSourceUserDto = new UserDto(1L, "alice", "alice@yandex.ru");
    }

    @Test
    void createTest() {
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
    void createWithBlankUserNameTest() {

        testSourceUserDto.setName("");
        BasicUserContext testContext = BasicUserContext.builder()
                .userDto(testSourceUserDto)
                .build();

        Throwable thrown = assertThrows(ValidationException.class, () -> {
            userService.create(testContext);
        });

        assertNotNull(thrown.getMessage());
    }

    @Test
    void retrieveTest() {

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
    void retrieveAllTest() {

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
    void updateTest() {

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
    void deleteTest() {

        DeleteUserContext testContext = DeleteUserContext.builder()
                .targetUserId(1L)
                .build();

        userService.delete(testContext);

        Mockito.verify(userRepository, Mockito.times(1))
                .deleteById(anyLong());
    }

    @Test
    void externalRetrieveText() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(testSourceUser));

        User testResultUser = userService.retrieve(1L);

        assertNotNull(testResultUser, "Не возвращается результат создания записи.");
        assertThat(testResultUser.getId(), equalTo(testSourceUser.getId()));
        assertThat(testResultUser.getName(), equalTo(testSourceUser.getName()));
        assertThat(testResultUser.getEmail(), equalTo(testSourceUser.getEmail()));
        Mockito.verify(userRepository, Mockito.times(1))
                .findById(anyLong());

    }
}