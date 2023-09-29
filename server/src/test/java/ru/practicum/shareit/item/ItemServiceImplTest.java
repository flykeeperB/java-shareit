package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.TestDataGenerator.TestDataGenerator;
import ru.practicum.shareit.booking.mapping.BookingMapper;
import ru.practicum.shareit.booking.mapping.impl.BookingMapperImpl;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.contexts.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemExtraDto;
import ru.practicum.shareit.item.mapping.*;
import ru.practicum.shareit.item.mapping.impl.CommentMapperImpl;
import ru.practicum.shareit.item.mapping.impl.ItemMapperImpl;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.impl.ItemServiceImpl;
import ru.practicum.shareit.item.validators.BookerValidator;
import ru.practicum.shareit.item.validators.OnlyItemOwnerValidator;
import ru.practicum.shareit.item.validators.impl.BookerValidatorImpl;
import ru.practicum.shareit.item.validators.impl.OnlyItemOwnerValidatorImpl;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {

    private final TestDataGenerator testDataGenerator = new TestDataGenerator();

    private ItemServiceImpl itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    private User testUser;
    private Item testItem;
    private ItemDto testItemDto;

    @BeforeEach
    public void setUp() {

        ItemMapper itemMapper = new ItemMapperImpl();
        CommentMapper commentMapper = new CommentMapperImpl();
        BookingMapper bookingMapper = new BookingMapperImpl();
        UserMapper userMapper = new UserMapperImpl();

        OnlyItemOwnerValidator onlyItemOwnerValidator = new OnlyItemOwnerValidatorImpl();
        BookerValidator bookerValidator = new BookerValidatorImpl();

        itemService = new ItemServiceImpl(itemRepository,
                userRepository,
                bookingRepository,
                commentRepository,
                itemRequestRepository,
                itemMapper,
                commentMapper,
                bookingMapper,
                userMapper,
                onlyItemOwnerValidator,
                bookerValidator
        );

        testUser = testDataGenerator.generateUser();
        testItem = testDataGenerator.generateItem();
        testItemDto = testDataGenerator.generateItemDto();
    }

    @Test
    public void retrieveForOwnerTest() {
        List<Item> repositoryResult = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            repositoryResult.add(testDataGenerator.generateItem());
        }

        when(itemRepository.findByOwnerIdOrderById(anyLong(), any(Pageable.class))).thenReturn(repositoryResult);

        List<Booking> bookingRepositoryResult = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            bookingRepositoryResult.add(testDataGenerator.generateBooking());
        }

        when(bookingRepository.findLastBookingsForItems(any(List.class),
                eq(BookingStatus.APPROVED),
                any(LocalDateTime.class))).thenReturn(bookingRepositoryResult);

        when(bookingRepository.findNextBookingsForItems(any(List.class),
                eq(BookingStatus.APPROVED),
                any(LocalDateTime.class))).thenReturn(bookingRepositoryResult);

        RetrieveItemForOwnerContext testContext = RetrieveItemForOwnerContext.builder()
                .sharerUserId(1L)
                .size(10)
                .from(1)
                .build();

        List<ItemExtraDto> testResult = itemService.retrieveForOwner(testContext);

        assertNotNull(testResult, "Не возвращается результат.");
        assertEquals(repositoryResult.size(), testResult.size(), "Неверное количество записей в результате");
        assertThat(testResult.get(0).getId(), equalTo(repositoryResult.get(0).getId()));

        Mockito.verify(itemRepository, Mockito.times(1))
                .findByOwnerIdOrderById(anyLong(), any(Pageable.class));

    }

    @Test
    public void retrieveAvailableForSearchTextTest() {
        List<Item> repositoryResult = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            repositoryResult.add(testDataGenerator.generateItem());
        }

        when(itemRepository
                .findAvailableByNameOrDescriptionContainingSearchText(anyString(), any(Pageable.class)))
                .thenReturn(repositoryResult);

        RetrieveAvailableForSearchTextContext testContext = RetrieveAvailableForSearchTextContext.builder()
                .sharerUserId(1L)
                .searchText("test")
                .size(10)
                .from(1)
                .build();

        List<ItemDto> testResult = itemService.retrieveAvailableForSearchText(testContext);

        assertNotNull(testResult, "Не возвращается результат.");
        assertEquals(repositoryResult.size(), testResult.size(), "Неверное количество записей в результате");
        assertThat(testResult.get(0).getId(), equalTo(repositoryResult.get(0).getId()));

        Mockito.verify(itemRepository, Mockito.times(1))
                .findAvailableByNameOrDescriptionContainingSearchText(anyString(), any(Pageable.class));
    }

   /* @Test
    public void retrieveAvailableForEmptySearchTextTest() {

        RetrieveAvailableForSearchTextContext testContext = RetrieveAvailableForSearchTextContext.builder()
                .sharerUserId(1L)
                .searchText("")
                .size(10)
                .from(1)
                .build();

        List<ItemDto> testResult = itemService.retrieveAvailableForSearchText(testContext);

        assertNotNull(testResult, "Не возвращается результат.");
        assertEquals(0, testResult.size(), "Неверное количество записей в результате");

        Mockito.verify(itemRepository, never())
                .findAvailableByNameOrDescriptionContainingSearchText(anyString(), any(Pageable.class));
    }*/

    @Test
    public void createCommentTest() {

        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(testItem));
        when(userRepository.findById(any())).thenReturn(Optional.of(testUser));

        Comment testComment = testDataGenerator.generateComment();
        when(commentRepository.save(any(Comment.class)))
                .thenReturn(testComment);

        //Тестовый набор успешных бронирований
        List<Booking> testSuccessfulBookings = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Booking testBooking = testDataGenerator.generateBooking();
            testBooking.getBooker().setId(1L);
            testSuccessfulBookings.add(testBooking);
        }
        when(bookingRepository.findByBookerIdAndStatusAndEndBefore(anyLong(),
                Mockito.eq(BookingStatus.APPROVED),
                any(LocalDateTime.class)))
                .thenReturn(testSuccessfulBookings);

        CreateCommentContext testContext = CreateCommentContext.builder()
                .sharerUserId(1L)
                .comment(testDataGenerator.generateOnlyTextCommentDto())
                .targetItemId(1L)
                .build();

        CommentDto testResult = itemService.createComment(testContext);

        assertNotNull(testResult, "Не возвращается результат создания записи.");
        assertThat(testResult.getId(), equalTo(testComment.getId()));
        assertThat(testResult.getText(), equalTo(testComment.getText()));
        assertThat(testResult.getCreated(), equalTo(testComment.getCreated()));
        Mockito.verify(commentRepository, Mockito.times(1))
                .save(any(Comment.class));

    }

    @Test
    public void createTest() {
        when(itemRepository.save(any(Item.class))).thenReturn(testItem);
        when(userRepository.findById(any())).thenReturn(Optional.of(testUser));
        when(itemRequestRepository.findById(anyLong()))
                .thenReturn(Optional.of(testDataGenerator.generateItemRequest()));

        testItemDto.setId(null);
        testItemDto.setRequestId(1L);

        CreateItemContext testContext = CreateItemContext.builder()
                .itemDto(testItemDto)
                .sharerUserId(1L)
                .build();

        ItemDto testResult = itemService.create(testContext);

        assertNotNull(testResult, "Не возвращается результат создания записи.");
        assertThat(testResult.getId(), equalTo(testItem.getId()));
        assertThat(testResult.getName(), equalTo(testItem.getName()));
        assertThat(testResult.getDescription(), equalTo(testItem.getDescription()));
        Mockito.verify(itemRepository, Mockito.times(1))
                .save(any(Item.class));
    }

    @Test
    public void retrieveTest() {

        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(testItem));

        List<Booking> bookingRepositoryResult = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            bookingRepositoryResult.add(testDataGenerator.generateBooking());
        }

        when(bookingRepository.findByItemIdAndStatusAndStartBeforeOrderByEndDesc(anyLong(),
                eq(BookingStatus.APPROVED),
                any(LocalDateTime.class))).thenReturn(bookingRepositoryResult);

        when(bookingRepository.findFirstByItemIdAndStatusAndStartAfterOrderByStart(anyLong(),
                eq(BookingStatus.APPROVED),
                any(LocalDateTime.class))).thenReturn(bookingRepositoryResult);

        BasicItemContext testContext = BasicItemContext.builder()
                .targetItemId(1L)
                .sharerUserId(1L)
                .build();

        ItemDto testResult = itemService.retrieve(testContext);

        assertNotNull(testResult, "Не возвращается результат.");
        assertThat(testResult.getId(), equalTo(testItem.getId()));
        assertThat(testResult.getName(), equalTo(testItem.getName()));
        assertThat(testResult.getDescription(), equalTo(testItem.getDescription()));
        Mockito.verify(itemRepository, Mockito.times(1))
                .findById(anyLong());
    }

    @Test
    public void updateTest() {
        testItem.getOwner().setId(1L);
        //для получение существующей записи перед обновлением
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(testItem));
        //сохранение обновленной записи
        when(itemRepository.save(any(Item.class))).thenReturn(testItem);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));

        UpdateItemContext testContext = UpdateItemContext.builder()
                .sharerUserId(1L)
                .itemDto(testItemDto)
                .targetItemId(testItemDto.getId())
                .build();

        ItemDto testResult = itemService.update(testContext);

        assertNotNull(testResult, "Не возвращается результат.");
        assertThat(testResult.getId(), equalTo(testItem.getId()));
        assertThat(testResult.getName(), equalTo(testItem.getName()));
        assertThat(testResult.getDescription(), equalTo(testItem.getDescription()));
        Mockito.verify(itemRepository, Mockito.times(1))
                .findById(anyLong());
        Mockito.verify(userRepository, Mockito.times(1))
                .findById(anyLong());
        Mockito.verify(itemRepository, Mockito.times(1))
                .save(any(Item.class));
    }

    @Test
    public void updateNoNewDataTest() {
        testItem.getOwner().setId(1L);
        //для получение существующей записи перед обновлением
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(testItem));
        when(userRepository.findById(any())).thenReturn(Optional.of(testUser));

        UpdateItemContext testContext = UpdateItemContext.builder()
                .sharerUserId(1L)
                .itemDto(null)
                .targetItemId(testItemDto.getId())
                .build();

        ItemDto testResult = itemService.update(testContext);

        assertNotNull(testResult, "Не возвращается результат.");
        assertThat(testResult.getId(), equalTo(testItem.getId()));
        assertThat(testResult.getName(), equalTo(testItem.getName()));
        assertThat(testResult.getDescription(), equalTo(testItem.getDescription()));
        Mockito.verify(itemRepository, Mockito.times(2))
                .findById(anyLong());
        Mockito.verify(itemRepository, Mockito.never())
                .save(any(Item.class));
    }

    @Test
    public void deleteTest() {
        BasicItemContext testContext = BasicItemContext.builder()
                .targetItemId(1L)
                .sharerUserId(1L)
                .build();

        itemService.delete(testContext);

        Mockito.verify(itemRepository, Mockito.times(1))
                .deleteById(anyLong());
    }

    @Test
    public void retrieveForExternalTest() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.ofNullable(testItem));

        Item testResult = itemService.retrieve(1L);

        assertNotNull(testResult, "Не возвращается результат создания записи.");
        assertThat(testResult.getId(), equalTo(testItem.getId()));
        Mockito.verify(itemRepository, Mockito.times(1))
                .findById(anyLong());
    }
}
