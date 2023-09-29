package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.TestDataGenerator.TestDataGenerator;
import ru.practicum.shareit.item.contexts.CreateItemContext;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
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
public class IntegrationItemServiceTest {

    private final EntityManager em;
    private final TestDataGenerator testDataGenerator = new TestDataGenerator();
    private final UserService userService;
    private final ItemService itemService;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    private UserDto testUserDto;
    private UserDto itemOwnerUserDto;
    private ItemDto testItemDto;

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

        testItemDto = testDataGenerator.generateItemDto();
        testItemDto.setOwner(itemOwnerUserDto);
        testItemDto.setId(null);
        testItemDto = itemService.create(CreateItemContext
                .builder()
                .itemDto(testItemDto)
                .sharerUserId(itemOwnerUserDto.getId())
                .build());
    }

    @AfterEach
    public void afterEach() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    public void createItemTest() {

        testItemDto = testDataGenerator.generateItemDto();
        testItemDto.setOwner(itemOwnerUserDto);
        testItemDto.setId(null);
        testItemDto = itemService.create(CreateItemContext
                .builder()
                .itemDto(testItemDto)
                .sharerUserId(itemOwnerUserDto.getId())
                .build());

        TypedQuery<Item> query = em
                .createQuery("Select i from Item i where i.owner.id = :ownerId and i.name = :name",
                        Item.class);
        Item item = query
                .setParameter("ownerId", itemOwnerUserDto.getId())
                .setParameter("name", testItemDto.getName())
                .getSingleResult();

        assertThat(item.getId(), notNullValue());
        assertThat(item.getName(), equalTo(testItemDto.getName()));
        assertThat(item.getDescription(), equalTo(testItemDto.getDescription()));
        assertThat(item.getAvailable(), equalTo(testItemDto.getAvailable()));
    }


}
