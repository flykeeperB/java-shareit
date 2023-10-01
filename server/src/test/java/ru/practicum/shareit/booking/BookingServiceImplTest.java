package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.TestDataGenerator.TestDataGenerator;
import ru.practicum.shareit.booking.contexts.ApproveBookingContext;
import ru.practicum.shareit.booking.contexts.BasicBookingContext;
import ru.practicum.shareit.booking.contexts.CreateBookingContext;
import ru.practicum.shareit.booking.contexts.ForStateBookingContext;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingExtraDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.mapping.BookingMapper;
import ru.practicum.shareit.booking.mapping.impl.BookingMapperImpl;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.impl.BookingServiceImpl;
import ru.practicum.shareit.booking.validators.*;
import ru.practicum.shareit.booking.validators.impl.*;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.mapping.ItemMapper;
import ru.practicum.shareit.item.mapping.impl.ItemMapperImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.mapping.UserMapper;
import ru.practicum.shareit.user.mapping.impl.UserMapperImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {

    private final TestDataGenerator testDataGenerator = new TestDataGenerator();

    private final BookingMapper bookingMapper = new BookingMapperImpl();
    private final UserMapper userMapper = new UserMapperImpl();
    private final ItemMapper itemMapper = new ItemMapperImpl();

    private final AvailabilityForBookingValidator availabilityForBookingValidator =
            new AvailabilityForBookingValidatorImpl();

    private final RelatedToBookedItemUserValidator relatedToBookedItemUserValidator =
            new RelatedToBookedItemUserValidatorImpl();
    private final OwnerOfBookedItemValidator ownerOfBookedItemValidator =
            new OwnerOfBookedItemValidatorImpl();
    private final AlreadyApprovedBookingValidator alreadyApprovedBookingValidator =
            new AlreadyApprovedBookingValidatorImpl();

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    private BookingServiceImpl bookingService;

    private Booking testBooking;
    private BookingExtraDto testBookingDto;
    private User testUser;

    @BeforeEach
    public void setUp() {

        bookingService = new BookingServiceImpl(bookingRepository,
                userRepository,
                itemRepository,
                bookingMapper,
                userMapper,
                itemMapper,
                availabilityForBookingValidator,
                relatedToBookedItemUserValidator,
                ownerOfBookedItemValidator,
                alreadyApprovedBookingValidator
        );

        testBooking = testDataGenerator.generateBooking();
        testBookingDto = testDataGenerator.generateBookingDto();
        testUser = testDataGenerator.generateUser();
    }

    @Test
    public void createTest() {
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);
        Item testItem = testDataGenerator.generateItem();
        when(userRepository.findById(any())).thenReturn(Optional.of(testUser));
        when(itemRepository.findById(any())).thenReturn(Optional.of(testItem));

        testBookingDto.setId(null);
        testBookingDto.setStart(testBooking.getStart());
        testBookingDto.setEnd(testBooking.getEnd());

        CreateBookingContext testContext = CreateBookingContext.builder()
                .bookingExtraDto(testBookingDto)
                .sharerUserId(1L)
                .build();

        BookingDto testResult = bookingService.create(testContext);

        assertNotNull(testResult, "Не возвращается результат создания записи.");
        assertThat(testResult.getId(), equalTo(testBooking.getId()));
        assertThat(testResult.getStart(), equalTo(testBooking.getStart()));
        assertThat(testResult.getEnd(), equalTo(testBooking.getEnd()));
        Mockito.verify(bookingRepository, Mockito.times(1))
                .save(any(Booking.class));
    }

    @Test
    public void createByOwnerTest() {
        Item testItem = testDataGenerator.generateItem();
        testItem.getOwner().setId(1L);
        when(userRepository.findById(any())).thenReturn(Optional.of(testUser));
        when(itemRepository.findById(any())).thenReturn(Optional.of(testItem));

        testBookingDto.setId(null);

        CreateBookingContext testContext = CreateBookingContext.builder()
                .bookingExtraDto(testBookingDto)
                .sharerUserId(1L)
                .build();

        assertThrows(NotFoundException.class, () -> bookingService.create(testContext));

    }

    @Test
    public void createNotAvailableTest() {
        Item testItem = testDataGenerator.generateItem();
        testItem.setAvailable(false);
        when(userRepository.findById(any())).thenReturn(Optional.of(testUser));
        when(itemRepository.findById(any())).thenReturn(Optional.of(testItem));

        testBookingDto.setId(null);

        CreateBookingContext testContext = CreateBookingContext.builder()
                .bookingExtraDto(testBookingDto)
                .sharerUserId(1L)
                .build();

        assertThrows(ValidationException.class, () -> bookingService.create(testContext));

    }

    /*@Test
    public void createWithEqualsStartAndEndDatesTest() {
        Item testItem = testDataGenerator.generateItem();
        when(userRepository.findById(any())).thenReturn(Optional.of(testUser));
        when(itemRepository.findById(any())).thenReturn(Optional.of(testItem));

        testBookingDto.setId(null);
        testBookingDto.setStart(testBooking.getStart());
        testBookingDto.setEnd(testBooking.getStart());

        CreateBookingContext testContext = CreateBookingContext.builder()
                .bookingExtraDto(testBookingDto)
                .sharerUserId(1L)
                .build();

        assertThrows(ValidationException.class, () -> bookingService.create(testContext));

    }*/

    /*@Test
    public void createWithWrongDatesTest() {
        Item testItem = testDataGenerator.generateItem();
        when(userRepository.findById(any())).thenReturn(Optional.of(testUser));
        when(itemRepository.findById(any())).thenReturn(Optional.of(testItem));

        testBookingDto.setId(null);
        testBookingDto.setStart(testBooking.getEnd());
        testBookingDto.setEnd(testBooking.getStart());

        CreateBookingContext testContext = CreateBookingContext.builder()
                .bookingExtraDto(testBookingDto)
                .sharerUserId(1L)
                .build();

        assertThrows(ValidationException.class, () -> bookingService.create(testContext));

    }*/

    /*@Test
    public void createWithWrongStartDateTest() {
        Item testItem = testDataGenerator.generateItem();
        when(userRepository.findById(any())).thenReturn(Optional.of(testUser));
        when(itemRepository.findById(any())).thenReturn(Optional.of(testItem));

        testBookingDto.setId(null);
        testBookingDto.setStart(LocalDateTime.now().minusMinutes(1));
        testBookingDto.setEnd(testBooking.getEnd());

        CreateBookingContext testContext = CreateBookingContext.builder()
                .bookingExtraDto(testBookingDto)
                .sharerUserId(1L)
                .build();

        assertThrows(ValidationException.class, () -> bookingService.create(testContext));

    }*/

    @Test
    public void retrieveTest() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(testBooking));
        when(userRepository.findById(any())).thenReturn(Optional.of(testUser));

        BasicBookingContext testContext = BasicBookingContext.builder()
                .targetBookingId(testBooking.getId())
                .sharerUserId(1L)
                .build();

        BookingExtraDto testResult = bookingService.retrieve(testContext);

        assertNotNull(testResult, "Не возвращается результат.");
        assertThat(testResult.getId(), equalTo(testBooking.getId()));
        assertThat(testResult.getItem().getId(), equalTo(testBooking.getItem().getId()));
        assertThat(testResult.getBooker().getId(), equalTo(testBooking.getBooker().getId()));
        Mockito.verify(bookingRepository, Mockito.times(1))
                .findById(anyLong());
    }

    @Test
    public void retrieveAllTest() {
        when(bookingRepository.findAll()).thenReturn(List.of(testBooking));

        List<BookingExtraDto> testResult = bookingService.retrieve();

        assertNotNull(testResult, "Не возвращается результат.");
        assertEquals(1, testResult.size(), "Неверное количество записей в результате");

        BookingExtraDto oneTestResult = testResult.get(0);

        assertThat(oneTestResult.getId(), equalTo(testBooking.getId()));

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findAll();
    }

    @Test
    public void deleteTest() {
        BasicBookingContext testContext = BasicBookingContext.builder()
                .targetBookingId(1L)
                .build();

        bookingService.delete(testContext);

        Mockito.verify(bookingRepository, Mockito.times(1))
                .deleteById(anyLong());
    }

    @Test
    public void approveTest() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(testBooking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        ApproveBookingContext testContext = ApproveBookingContext.builder()
                .targetBookingId(1L)
                .sharerUserId(1L)
                .isApproved(true)
                .build();

        bookingService.approve(testContext);

        Mockito.verify(bookingRepository, Mockito.times(1))
                .save(any(Booking.class));
    }

    @Test
    public void retrieveForBookerTest() {
        User testUser = testDataGenerator.generateUser();
        when(userRepository.findById(any())).thenReturn(Optional.of(testUser));

        List<Booking> repositoryResult = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            repositoryResult.add(testDataGenerator.generateBooking());
        }

        when(bookingRepository.findByBookerIdAndEndBeforeOrderByStartDesc(anyLong(),
                any(LocalDateTime.class),
                any(Pageable.class))).thenReturn(repositoryResult);

        when(bookingRepository.findByBookerIdAndStartAfterOrderByStartDesc(anyLong(),
                any(LocalDateTime.class),
                any(Pageable.class))).thenReturn(repositoryResult);

        when(bookingRepository.findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(),
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                any(Pageable.class))).thenReturn(repositoryResult);

        when(bookingRepository.findByBookerIdAndStatusOrderByStartDesc(anyLong(),
                any(BookingStatus.class),
                any(Pageable.class))).thenReturn(repositoryResult);

        when(bookingRepository.findByBookerIdAndStatusAndStartBefore(anyLong(),
                any(BookingStatus.class),
                any(LocalDateTime.class),
                any(Pageable.class))).thenReturn(repositoryResult);

        when(bookingRepository.findByBookerIdAndStatusAndEndBefore(anyLong(),
                any(BookingStatus.class),
                any(LocalDateTime.class),
                any(Pageable.class))).thenReturn(repositoryResult);

        when(bookingRepository.findByBookerIdOrderByStartDesc(anyLong(),
                any(Pageable.class))).thenReturn(repositoryResult);

        for (State state : State.values()) {
            retrieveForBookerByState(state, repositoryResult);
        }

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findByBookerIdAndStatusAndStartBefore(anyLong(),
                        any(BookingStatus.class),
                        any(LocalDateTime.class),
                        any(Pageable.class));

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findByBookerIdAndEndBeforeOrderByStartDesc(anyLong(),
                        any(LocalDateTime.class),
                        any(Pageable.class));

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findByBookerIdAndStartAfterOrderByStartDesc(anyLong(),
                        any(LocalDateTime.class),
                        any(Pageable.class));

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(),
                        any(LocalDateTime.class),
                        any(LocalDateTime.class),
                        any(Pageable.class));

        Mockito.verify(bookingRepository, Mockito.times(2))
                .findByBookerIdAndStatusOrderByStartDesc(anyLong(),
                        any(BookingStatus.class),
                        any(Pageable.class));

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findByBookerIdAndStatusAndStartBefore(anyLong(),
                        any(BookingStatus.class),
                        any(LocalDateTime.class),
                        any(Pageable.class));

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findByBookerIdAndStatusAndEndBefore(anyLong(),
                        any(BookingStatus.class),
                        any(LocalDateTime.class),
                        any(Pageable.class));

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findByBookerIdOrderByStartDesc(anyLong(),
                        any(Pageable.class));
    }

    @Test
    public void retrieveForItemsOwnerTest() {
        User testUser = testDataGenerator.generateUser();
        when(userRepository.findById(any())).thenReturn(Optional.of(testUser));

        List<Booking> repositoryResult = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            repositoryResult.add(testDataGenerator.generateBooking());
        }

        when(bookingRepository.findByItemOwnerIdAndEndBeforeOrderByStartDesc(anyLong(),
                any(LocalDateTime.class),
                any(Pageable.class))).thenReturn(repositoryResult);

        when(bookingRepository.findByItemOwnerIdAndStartAfterOrderByStartDesc(anyLong(),
                any(LocalDateTime.class),
                any(Pageable.class))).thenReturn(repositoryResult);

        when(bookingRepository.findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(),
                any(LocalDateTime.class),
                any(LocalDateTime.class),
                any(Pageable.class))).thenReturn(repositoryResult);

        when(bookingRepository.findByItemOwnerIdAndStatusOrderByStartDesc(anyLong(),
                any(BookingStatus.class),
                any(Pageable.class))).thenReturn(repositoryResult);

        when(bookingRepository.findByItemOwnerIdAndStatusAndStartBefore(anyLong(),
                any(BookingStatus.class),
                any(LocalDateTime.class),
                any(Pageable.class))).thenReturn(repositoryResult);

        when(bookingRepository.findByItemOwnerIdAndStatusAndEndBefore(anyLong(),
                any(BookingStatus.class),
                any(LocalDateTime.class),
                any(Pageable.class))).thenReturn(repositoryResult);

        when(bookingRepository.findByItemOwnerIdOrderByStartDesc(anyLong(),
                any(Pageable.class))).thenReturn(repositoryResult);

        for (State state : State.values()) {
            retrieveForOwnerByState(state, repositoryResult);
        }


        Mockito.verify(bookingRepository, Mockito.times(1))
                .findByItemOwnerIdAndEndBeforeOrderByStartDesc(anyLong(),
                        any(LocalDateTime.class),
                        any(Pageable.class));

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findByItemOwnerIdAndStartAfterOrderByStartDesc(anyLong(),
                        any(LocalDateTime.class),
                        any(Pageable.class));

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(anyLong(),
                        any(LocalDateTime.class),
                        any(LocalDateTime.class),
                        any(Pageable.class));

        Mockito.verify(bookingRepository, Mockito.times(2))
                .findByItemOwnerIdAndStatusOrderByStartDesc(anyLong(),
                        any(BookingStatus.class),
                        any(Pageable.class));

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findByItemOwnerIdAndStatusAndStartBefore(anyLong(),
                        any(BookingStatus.class),
                        any(LocalDateTime.class),
                        any(Pageable.class));

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findByItemOwnerIdAndStatusAndEndBefore(anyLong(),
                        any(BookingStatus.class),
                        any(LocalDateTime.class),
                        any(Pageable.class));

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findByItemOwnerIdOrderByStartDesc(anyLong(),
                        any(Pageable.class));

    }

    private void retrieveForOwnerByState(State testState, List<Booking> repositoryResult) {
        ForStateBookingContext testContext = ForStateBookingContext.builder()
                .sharerUserId(1L)
                .state(testState)
                .from(1)
                .size(10)
                .build();

        List<BookingExtraDto> testResult = bookingService.retrieveForItemsOwner(testContext);

        assertNotNull(testResult, "Не возвращается результат.");
        assertEquals(10, testResult.size(), "Неверное количество записей в результате");
        assertThat(testResult.get(0).getId(), equalTo(repositoryResult.get(0).getId()));
    }

    private void retrieveForBookerByState(State testState, List<Booking> repositoryResult) {
        ForStateBookingContext testContext = ForStateBookingContext.builder()
                .sharerUserId(1L)
                .state(testState)
                .from(1)
                .size(10)
                .build();

        List<BookingExtraDto> testResult = bookingService.retrieveForBooker(testContext);

        assertNotNull(testResult, "Не возвращается результат.");
        assertEquals(10, testResult.size(), "Неверное количество записей в результате");
        assertThat(testResult.get(0).getId(), equalTo(repositoryResult.get(0).getId()));

    }

}
