package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.TestDataGenerator.TestDataGenerator;
import ru.practicum.shareit.booking.contexts.CreateBookingContext;
import ru.practicum.shareit.booking.dto.BookingExtraDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.contexts.CreateItemContext;
import ru.practicum.shareit.item.dto.ItemDto;
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
public class IntegrationBookingServiceTest {

    private final EntityManager em;
    private final TestDataGenerator testDataGenerator = new TestDataGenerator();
    private final UserService userService;
    private final ItemService itemService;
    private final BookingService bookingService;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;

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
        bookingRepository.deleteAll();
    }

    @Test
    public void createBookingTest() {
        BookingExtraDto bookingDto = testDataGenerator.generateBookingDto();
        bookingDto.setBookerId(testUserDto.getId());
        bookingDto.setBooker(testUserDto);
        bookingDto.setItemId(testItemDto.getId());
        bookingDto.setItem(testItemDto);
        CreateBookingContext context = CreateBookingContext.builder()
                .sharerUserId(testUserDto.getId())
                .bookingExtraDto(bookingDto)
                .build();

        bookingService.create(context);

        TypedQuery<Booking> query = em
                .createQuery("Select b from Booking b where b.booker.id = :bookerId and b.item.id = :itemId",
                        Booking.class);
        Booking booking = query
                .setParameter("bookerId", bookingDto.getBooker().getId())
                .setParameter("itemId", bookingDto.getItem().getId())
                .getSingleResult();

        assertThat(booking.getId(), notNullValue());
        assertThat(booking.getStart(), equalTo(bookingDto.getStart()));
        assertThat(booking.getEnd(), equalTo(bookingDto.getEnd()));
        assertThat(booking.getStatus(), equalTo(bookingDto.getStatus()));

    }


}
