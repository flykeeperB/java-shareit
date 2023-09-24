package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.TestDataGenerator.TestDataGenerator;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private TestDataGenerator testDataGenerator = new TestDataGenerator();

    private User testItemOwner;
    private Item testItem;
    private List<Item> testItems = new ArrayList<>();

    @BeforeEach
    public void setUp() {

        testItemOwner = testDataGenerator.generateUser();
        testItemOwner.setId(null);
        testItemOwner = userRepository.save(testItemOwner);

        testItem = testDataGenerator.generateItem();
        testItem.setRequest(null);
        testItem.setId(null);
        testItem.setOwner(testItemOwner);
        testItem = itemRepository.save(testItem);

        //вещи с описанием 1
        for (int i = 0; i < 5; i++) {
            Item item = testDataGenerator.generateItem();
            item.setOwner(testItemOwner);
            item.setId(null);
            item.setName("Дирижабль");
            item.setDescription("превосходный дирижабль");
            testItems.add(itemRepository.save(item));
        }

        //вещи с описанием 2
        for (int i = 0; i < 5; i++) {
            Item item = testDataGenerator.generateItem();
            item.setOwner(testItemOwner);
            item.setId(null);
            item.setName("Перчатки");
            item.setDescription("обычные перчатки");
            testItems.add(itemRepository.save(item));
        }
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    public void searchByTextTest() {

        Pageable pageable = Pageable.ofSize(10);

        List<Item> result = itemRepository.findAvailableByNameOrDescriptionContainingSearchText(
                        "дириж", pageable);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(5, result.size());
    }

    @Test
    public void searchByWrongTextTest() {

        Pageable pageable = Pageable.ofSize(10);

        List<Item> result = itemRepository.findAvailableByNameOrDescriptionContainingSearchText(
                "самолет", pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
