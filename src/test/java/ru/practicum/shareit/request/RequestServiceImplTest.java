package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.TestDataGenerator.TestDataGenerator;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.mapping.ItemMapper;
import ru.practicum.shareit.item.mapping.impl.ItemMapperImpl;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.contexts.CreateItemRequestContext;
import ru.practicum.shareit.request.contexts.RetrieveItemRequestContext;
import ru.practicum.shareit.request.contexts.RetrieveItemRequestsContext;
import ru.practicum.shareit.request.contexts.RetrieveItemRequestsForUserContext;
import ru.practicum.shareit.request.dto.ExtraItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapping.*;
import ru.practicum.shareit.request.mapping.impl.ItemRequestMapperImpl;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.impl.ItemRequestServiceImpl;
import ru.practicum.shareit.request.validators.NotBlankDescriptionOfItemRequestValidator;
import ru.practicum.shareit.request.validators.impl.NotBlankDescriptionOfItemRequestValidatorImpl;
import ru.practicum.shareit.user.mapping.UserMapper;
import ru.practicum.shareit.user.mapping.impl.UserMapperImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RequestServiceImplTest {

    private final TestDataGenerator testDataGenerator = new TestDataGenerator();

    private ItemRequestServiceImpl itemRequestService;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    private User testUser;
    private ItemRequest testItemRequest;
    private ItemRequestDto testItemRequestDto;

    @BeforeEach
    public void setUp() {

        ItemMapper itemMapper = new ItemMapperImpl();
        ItemRequestMapper itemRequestMapper = new ItemRequestMapperImpl();
        UserMapper userMapper = new UserMapperImpl();

        NotBlankDescriptionOfItemRequestValidator notBlankDescriptionOfItemRequestValidator = new NotBlankDescriptionOfItemRequestValidatorImpl();

        itemRequestService = new ItemRequestServiceImpl(itemRequestRepository,
                userRepository,
                itemRepository,
                itemRequestMapper,
                userMapper,
                itemMapper,
                notBlankDescriptionOfItemRequestValidator
        );

        testUser = testDataGenerator.generateUser();
        testItemRequest = testDataGenerator.generateItemRequest();
        testItemRequestDto = testDataGenerator.generateItemRequestDto();
    }

    @Test
    public void createTest() {

        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(testItemRequest);
        when(userRepository.findById(any())).thenReturn(Optional.of(testUser));

        testItemRequestDto.setId(null);

        CreateItemRequestContext testContext = CreateItemRequestContext.builder()
                .itemRequestDto(testDataGenerator.generateItemRequestDto())
                .sharerUserId(1L)
                .build();

        ItemRequestDto testResult = itemRequestService.create(testContext);

        assertNotNull(testResult, "Не возвращается результат создания записи.");
        assertThat(testResult.getId(), equalTo(testItemRequest.getId()));
        assertThat(testResult.getDescription(), equalTo(testItemRequest.getDescription()));

        Mockito.verify(itemRequestRepository, Mockito.times(1))
                .save(any(ItemRequest.class));
    }

    @Test
    public void createWithBlankDescriptionTest() {

        when(userRepository.findById(any())).thenReturn(Optional.of(testUser));

        testItemRequestDto.setDescription(null);

        CreateItemRequestContext testContext = CreateItemRequestContext.builder()
                .itemRequestDto(testItemRequestDto)
                .sharerUserId(1L)
                .build();

        assertThrows(ValidationException.class, () -> {
            itemRequestService.create(testContext);
        });
    }

    @Test
    public void retrieveForUserTest() {
        testUser.setId(1L);
        when(userRepository.findById(any())).thenReturn(Optional.of(testUser));

        //Тестовый набор запросов пользователя
        List<ItemRequest> repositoryResult = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ItemRequest itemRequest = testDataGenerator.generateItemRequest();
            itemRequest.getRequestor().setId(1L);
            repositoryResult.add(itemRequest);
        }
        when(itemRequestRepository.findRequestByRequestorIdOrderByCreatedDesc(anyLong()))
                .thenReturn(repositoryResult);

        RetrieveItemRequestsForUserContext testContext = RetrieveItemRequestsForUserContext.builder()
                .sharerUserId(1L)
                .build();

        List<ExtraItemRequestDto> testResult = itemRequestService.retrieve(testContext);

        assertNotNull(testResult, "Не возвращается результат.");
        assertEquals(repositoryResult.size(), testResult.size(), "Неверное количество записей в результате");

        Mockito.verify(itemRequestRepository, Mockito.times(1))
                .findRequestByRequestorIdOrderByCreatedDesc(anyLong());
    }

    @Test
    public void retrieveRequestsTest() {
        testUser.setId(1L);
        when(userRepository.findById(any())).thenReturn(Optional.of(testUser));

        //Тестовый набор чужих запросов на предоставление вещей
        List<ItemRequest> repositoryResult = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ItemRequest itemRequest = testDataGenerator.generateItemRequest();
            repositoryResult.add(itemRequest);
        }
        Page<ItemRequest> pageRepositoryResult = new PageImpl<>(repositoryResult);
        when(itemRequestRepository.findByRequestorIdNot(anyLong(), any(Pageable.class)))
                .thenReturn(pageRepositoryResult);

        RetrieveItemRequestsContext testContext = RetrieveItemRequestsContext.builder()
                .sharerUserId(1L)
                .from(1)
                .size(10)
                .build();

        List<ExtraItemRequestDto> testResult = itemRequestService.retrieve(testContext);

        assertNotNull(testResult, "Не возвращается результат.");
        assertEquals(repositoryResult.size(), testResult.size(), "Неверное количество записей в результате");

        Mockito.verify(itemRequestRepository, Mockito.times(1))
                .findByRequestorIdNot(anyLong(), any(Pageable.class));
    }

    @Test
    public void retrieve() {
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.ofNullable(testItemRequest));
        when(userRepository.findById(any())).thenReturn(Optional.of(testUser));

        RetrieveItemRequestContext testContext = RetrieveItemRequestContext.builder()
                .targetItemRequestId(1L)
                .sharerUserId(1L)
                .build();

        ExtraItemRequestDto testResult = itemRequestService.retrieve(testContext);

        assertNotNull(testResult, "Не возвращается результат.");
        assertThat(testResult.getId(), equalTo(testItemRequest.getId()));
        assertThat(testResult.getDescription(), equalTo(testItemRequest.getDescription()));

        Mockito.verify(itemRequestRepository, Mockito.times(1))
                .findById(anyLong());
    }

}
