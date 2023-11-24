package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.TestDataGenerator.TestDataGenerator;
import ru.practicum.shareit.user.contexts.BasicUserContext;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class IntegrationUserServiceTest {

    private final EntityManager em;
    private final TestDataGenerator testDataGenerator = new TestDataGenerator();
    private final UserService userService;
    private final UserRepository userRepository;

    private UserDto testUserDto;

    @BeforeEach
    public void beforeEach() {
        testUserDto = testDataGenerator.generateUserDto();
        testUserDto.setId(null);
    }

    @AfterEach
    public void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    public void createUserTest() {
        testUserDto.setId(null);
        testUserDto = userService.create(BasicUserContext.builder()
                .userDto(testUserDto).build());

        TypedQuery<User> query = em
                .createQuery("Select u from User u where " +
                                " u.email = :email",
                        User.class);
        User result = query
                .setParameter("email", testUserDto.getEmail())
                .getSingleResult();

        assertThat(result.getId(), notNullValue());
        assertThat(result.getName(), equalTo(testUserDto.getName()));
        assertThat(result.getEmail(), equalTo(testUserDto.getEmail()));
    }
}
