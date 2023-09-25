package ru.practicum.shareit.booking;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.TestDataGenerator.TestDataGenerator;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class BookingRepositoryTest {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private TestDataGenerator testDataGenerator = new TestDataGenerator();

    private LocalDateTime testMoment;
    private User testUser;
    private User testItemOwner;
    private Item testItem;
    private List<Booking> testBookings = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        testUser = testDataGenerator.generateUser();
        testUser.setId(null);
        testUser = userRepository.save(testUser);

        testItemOwner = testDataGenerator.generateUser();
        testItemOwner.setId(null);
        testItemOwner = userRepository.save(testItemOwner);

        testItem = testDataGenerator.generateItem();
        testItem.setRequest(null);
        testItem.setId(null);
        testItem.setOwner(testItemOwner);
        testItem = itemRepository.save(testItem);

        testMoment = LocalDateTime.now();
        LocalDateTime moment = testMoment.minusDays(1);

        for (int i = 1; i < 20; i++) {
            Booking booking = testDataGenerator.generateBooking();
            booking.setId(null);
            booking.setItem(testItem);
            booking.setBooker(testUser);
            booking.setStatus(BookingStatus.APPROVED);
            booking.setStart(moment);
            moment = moment.plusHours(1);
            booking.setEnd(moment);
            moment = moment.plusHours(1);
            testBookings.add(bookingRepository.save(booking));
        }
    }

    @AfterEach
    public void tearDown() {
        testBookings.clear();
        bookingRepository.deleteAll();
        userRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @Test
    public void findLastBookingsForItemsTest() {
        List<Booking> result = bookingRepository
                .findLastBookingsForItems(List.of(testItem.getId()),
                        BookingStatus.APPROVED,
                        testMoment);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.get(0).getStart().isBefore(testMoment));
    }

    @Test
    public void findNextBookingsForItems() {
        List<Booking> result = bookingRepository
                .findNextBookingsForItems(List.of(testItem.getId()),
                        BookingStatus.APPROVED,
                        testMoment);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.get(0).getStart().isAfter(testMoment));
    }

}
