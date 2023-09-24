package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.TestDataGenerator.TestDataGenerator;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.contexts.CreateItemRequestContext;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.contexts.BasicUserContext;
import ru.practicum.shareit.user.dto.UserDto;
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
public class IntegrationRequestServiceTest {

    private final EntityManager em;
    private final TestDataGenerator testDataGenerator = new TestDataGenerator();
    private final UserService userService;
    private final ItemRequestService itemRequestService;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemRequestRepository itemRequestRepository;

    private UserDto testUserDto;
    private UserDto itemOwnerUserDto;
    private ItemRequestDto itemRequestDto;

    @BeforeEach
    public void beforeEach() {
        testUserDto = testDataGenerator.generateUserDto();
        testUserDto.setId(null);
        testUserDto = userService.create(BasicUserContext.builder()
                .userDto(testUserDto).build());

        itemOwnerUserDto = testDataGenerator.generateUserDto();
        itemOwnerUserDto.setId(null);
        itemOwnerUserDto = userService.create(BasicUserContext.builder()
                .userDto(itemOwnerUserDto).build());

        itemRequestDto = testDataGenerator.generateItemRequestDto();
        itemRequestDto.setRequestor(testUserDto);
    }

    @AfterEach
    public void afterEach() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        itemRequestRepository.deleteAll();
    }

    @Test
    public void createItemRequestTest() {
        itemRequestDto.setId(null);
        itemRequestDto = itemRequestService.create(CreateItemRequestContext
                .builder()
                .itemRequestDto(itemRequestDto)
                .sharerUserId(testUserDto.getId())
                .build());

        TypedQuery<ItemRequest> query = em
                .createQuery("Select r from ItemRequest r where " +
                                " r.requestor.id = :requestorId and r.description = :description",
                        ItemRequest.class);
        ItemRequest result = query
                .setParameter("requestorId", testUserDto.getId())
                .setParameter("description", itemRequestDto.getDescription())
                .getSingleResult();

        assertThat(result.getId(), notNullValue());
        assertThat(result.getDescription(), equalTo(itemRequestDto.getDescription()));
    }

}
