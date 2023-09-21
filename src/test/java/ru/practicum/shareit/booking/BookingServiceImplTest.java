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
import ru.practicum.shareit.booking.mapping.ToBookingExtraDtoMapper;
import ru.practicum.shareit.booking.mapping.ToBookingMapper;
import ru.practicum.shareit.booking.mapping.impl.ToBookingDtoMapperImpl;
import ru.practicum.shareit.booking.mapping.impl.ToBookingExtraDtoMapperImpl;
import ru.practicum.shareit.booking.mapping.impl.ToBookingMapperImpl;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.impl.BookingServiceImpl;
import ru.practicum.shareit.booking.service.impl.TypesOfBookingConnectionToItem;
import ru.practicum.shareit.booking.validators.*;
import ru.practicum.shareit.booking.validators.impl.*;
import ru.practicum.shareit.core.validators.SharerUserValidator;
import ru.practicum.shareit.core.validators.impl.SharerUserValidatorImpl;
import ru.practicum.shareit.item.mapping.ToItemDtoMapper;
import ru.practicum.shareit.item.mapping.impl.ToCommentDtoMapperImpl;
import ru.practicum.shareit.item.mapping.impl.ToItemDtoMapperImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ExternalItemService;
import ru.practicum.shareit.user.mapping.ToUserDtoMapper;
import ru.practicum.shareit.user.mapping.impl.ToUserDtoMapperImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.ExternalUserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceImplTest {

    private final TestDataGenerator testDataGenerator = new TestDataGenerator();

    private BookingServiceImpl bookingService;

    @Mock
    private BookingRepository bookingRepository;

    private AvailabilityForBookingValidator availabilityForBookingValidator;
    private CorrectnessOfBookingDatesValidator correctnessOfBookingDatesValidator;
    private SharerUserValidator sharerUserValidator;
    private RelatedToBookedItemUserValidator relatedToBookedItemUserValidator;
    private OwnerOfBookedItemValidator ownerOfBookedItemValidator;
    private AlreadyApprovedBookingValidator alreadyApprovedBookingValidator;

    private ToBookingMapper toBookingMapper;
    private ToBookingExtraDtoMapper toBookingExtraDtoMapper;

    private ToBookingDtoMapperImpl toBookingDtoMapper;
    private ToUserDtoMapper toUserDtoMapper;
    private ToItemDtoMapper toItemDtoMapper;

    @Mock
    private ExternalUserService userService;

    @Mock
    private ExternalItemService itemService;

    private Booking testBooking;
    private BookingExtraDto testBookingDto;

    @BeforeEach
    void setUp() {
        AvailabilityForBookingValidator availabilityForBookingValidator =
                new AvailabilityForBookingValidatorImpl();
        CorrectnessOfBookingDatesValidator correctnessOfBookingDatesValidator =
                new CorrectnessOfBookingDatesValidatorImpl();
        SharerUserValidator sharerUserValidator =
                new SharerUserValidatorImpl();
        RelatedToBookedItemUserValidator relatedToBookedItemUserValidator =
                new RelatedToBookedItemUserValidatorImpl();
        OwnerOfBookedItemValidator ownerOfBookedItemValidator =
                new OwnerOfBookedItemValidatorImpl();
        AlreadyApprovedBookingValidator alreadyApprovedBookingValidator =
                new AlreadyApprovedBookingValidatorImpl();

        ToBookingMapper toBookingMapper = new ToBookingMapperImpl();

        ToBookingExtraDtoMapper toBookingExtraDtoMapper = new ToBookingExtraDtoMapperImpl(
                new ToBookingDtoMapperImpl(),
                new ToUserDtoMapperImpl(),
                new ToItemDtoMapperImpl(
                        new ToUserDtoMapperImpl(),
                        new ToCommentDtoMapperImpl()
                )
        );

        bookingService = new BookingServiceImpl(bookingRepository,
                availabilityForBookingValidator,
                correctnessOfBookingDatesValidator,
                sharerUserValidator,
                relatedToBookedItemUserValidator,
                ownerOfBookedItemValidator,
                alreadyApprovedBookingValidator,
                toBookingMapper,
                toBookingExtraDtoMapper,
                userService,
                itemService
        );

        testBooking = testDataGenerator.generateBooking();
        testBookingDto = testDataGenerator.generateBookingDto();
    }

    @Test
    public void createTest() {
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);
        User testUser = testDataGenerator.generateUser();
        Item testItem = testDataGenerator.generateItem();
        when(userService.retrieve(any())).thenReturn(testUser);
        when(itemService.retrieve(any())).thenReturn(testItem);

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
    public void retrieveTest() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(testBooking));
        when(userService.retrieve(anyLong())).thenReturn(testDataGenerator.generateUser());

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
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);
        User testUser = testDataGenerator.generateUser();
        when(userService.retrieve(anyLong())).thenReturn(testUser);

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
        when(userService.retrieve(anyLong())).thenReturn(testUser);

        List<Booking> repositoryResult = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            repositoryResult.add(testDataGenerator.generateBooking());
        }

        when(bookingRepository.findByBookerIdAndStatusAndStartBefore(anyLong(),
                any(BookingStatus.class),
                any(LocalDateTime.class),
                any(Pageable.class))).thenReturn(repositoryResult);

        ForStateBookingContext testContext = ForStateBookingContext.builder()
                .sharerUserId(1L)
                .state(State.STARTED)
                .from(1)
                .size(10)
                .build();

        List<BookingExtraDto> testResult = bookingService.retrieveForBooker(testContext);

        assertNotNull(testResult, "Не возвращается результат.");
        assertEquals(10, testResult.size(), "Неверное количество записей в результате");
        assertThat(testResult.get(0).getId(), equalTo(repositoryResult.get(0).getId()));

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findByBookerIdAndStatusAndStartBefore(anyLong(),
                        eq(BookingStatus.APPROVED),
                        any(LocalDateTime.class),
                        any(Pageable.class));

    }

    @Test
    public void retrieveForItemsOwnerTest() {
        User testUser = testDataGenerator.generateUser();
        when(userService.retrieve(anyLong())).thenReturn(testUser);

        List<Booking> repositoryResult = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            repositoryResult.add(testDataGenerator.generateBooking());
        }

        when(bookingRepository.findByItemOwnerIdAndStatusAndStartBefore(anyLong(),
                any(BookingStatus.class),
                any(LocalDateTime.class),
                any(Pageable.class))).thenReturn(repositoryResult);

        ForStateBookingContext testContext =
                ForStateBookingContext.builder()
                        .sharerUserId(1L)
                        .state(State.STARTED)
                        .from(1)
                        .size(10)
                        .build();

        List<BookingExtraDto> testResult = bookingService.retrieveForItemsOwner(testContext);

        assertNotNull(testResult, "Не возвращается результат.");
        assertEquals(10, testResult.size(), "Неверное количество записей в результате");
        assertThat(testResult.get(0).getId(), equalTo(repositoryResult.get(0).getId()));

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findByItemOwnerIdAndStatusAndStartBefore(anyLong(),
                        eq(BookingStatus.APPROVED),
                        any(LocalDateTime.class),
                        any(Pageable.class));
    }

    @Test
    public void retrieveForItemTest() {

        List<Booking> repositoryResult1 = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            repositoryResult1.add(testDataGenerator.generateBooking());
        }

        when(bookingRepository.findByItemIdAndStatusAndStartBeforeOrderByEndDesc(anyLong(),
                any(BookingStatus.class),
                any(LocalDateTime.class))).thenReturn(repositoryResult1);

        List<Booking> repositoryResult2 = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            repositoryResult2.add(testDataGenerator.generateBooking());
        }

        when(bookingRepository.findFirstByItemIdAndStatusAndStartAfterOrderByStart(anyLong(),
                any(BookingStatus.class),
                any(LocalDateTime.class))).thenReturn(repositoryResult2);

        Map<TypesOfBookingConnectionToItem, Booking> testResult = bookingService.retrieveForItem(1L);

        assertNotNull(testResult, "Не возвращается результат.");
        assertNotNull(testResult.get(TypesOfBookingConnectionToItem.LAST),
                "Результат не содержит части данных (last booking)");
        assertNotNull(testResult.get(TypesOfBookingConnectionToItem.NEXT),
                "Результат не содержит части данных (next booking)");

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findByItemIdAndStatusAndStartBeforeOrderByEndDesc(anyLong(),
                        any(BookingStatus.class),
                        any(LocalDateTime.class));

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findFirstByItemIdAndStatusAndStartAfterOrderByStart(anyLong(),
                        any(BookingStatus.class),
                        any(LocalDateTime.class));
    }

    @Test
    public void retrieveForItemsTest() {
        List<Booking> repositoryResult = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Booking booking = testDataGenerator.generateBooking();
            booking.getItem().setId((long) i + 1);
            repositoryResult.add(booking);
        }

        when(bookingRepository.findLastBookingsForItems(any(List.class),
                any(BookingStatus.class),
                any(LocalDateTime.class))).thenReturn(repositoryResult);

        when(bookingRepository.findNextBookingsForItems(any(List.class),
                any(BookingStatus.class),
                any(LocalDateTime.class))).thenReturn(repositoryResult);

        Map<Long, Map<TypesOfBookingConnectionToItem, Booking>> testResult = bookingService.retrieveForItems(List.of(1L, 2L, 3L));

        assertNotNull(testResult, "Не возвращается результат.");
        assertEquals(3, testResult.size(), "Неверное количество записей в результате");
        assertNotNull(testResult.get(1L).get(TypesOfBookingConnectionToItem.LAST),
                "Результат не содержит части данных (last booking)");
        assertNotNull(testResult.get(1L).get(TypesOfBookingConnectionToItem.NEXT),
                "Результат не содержит части данных (next booking)");

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findLastBookingsForItems(any(List.class),
                        any(BookingStatus.class),
                        any(LocalDateTime.class));

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findNextBookingsForItems(any(List.class),
                        any(BookingStatus.class),
                        any(LocalDateTime.class));

    }

    @Test
    public void retrieveForExternalServicesTest() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.ofNullable(testBooking));

        Booking testResult = bookingService.retrieve(1L);

        assertNotNull(testResult, "Не возвращается результат создания записи.");
        assertThat(testResult.getId(), equalTo(testBooking.getId()));
        Mockito.verify(bookingRepository, Mockito.times(1))
                .findById(anyLong());
    }

    @Test
    public void retrieveSuccessfulBookingsTest() {

        List<Booking> repositoryResult = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Booking booking = testDataGenerator.generateBooking();
            repositoryResult.add(booking);
        }

        when(bookingRepository.findByBookerIdAndStatusAndEndBefore(anyLong(),
                any(BookingStatus.class),
                any(LocalDateTime.class))).thenReturn(repositoryResult);

        List<Booking> testResult = bookingService.retrieveSuccessfulBookings(1L);

        assertNotNull(testResult, "Не возвращается результат.");
        assertEquals(10, testResult.size(), "Неверное количество записей в результате");

        Mockito.verify(bookingRepository, Mockito.times(1))
                .findByBookerIdAndStatusAndEndBefore(anyLong(),
                        any(BookingStatus.class),
                        any(LocalDateTime.class));

    }
}
