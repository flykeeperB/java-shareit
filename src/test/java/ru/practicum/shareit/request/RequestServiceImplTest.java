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
import ru.practicum.shareit.core.validators.SharerUserValidator;
import ru.practicum.shareit.core.validators.impl.SharerUserValidatorImpl;
import ru.practicum.shareit.item.mapping.impl.*;
import ru.practicum.shareit.item.service.ExternalItemService;
import ru.practicum.shareit.request.contexts.CreateItemRequestContext;
import ru.practicum.shareit.request.contexts.RetrieveItemRequestContext;
import ru.practicum.shareit.request.contexts.RetrieveItemRequestsContext;
import ru.practicum.shareit.request.contexts.RetrieveItemRequestsForUserContext;
import ru.practicum.shareit.request.dto.ExtraItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapping.ToExtraItemRequestDtoListMapper;
import ru.practicum.shareit.request.mapping.ToExtraItemRequestDtoMapper;
import ru.practicum.shareit.request.mapping.ToItemRequestDtoMapper;
import ru.practicum.shareit.request.mapping.ToItemRequestMapper;
import ru.practicum.shareit.request.mapping.impl.ToExtraItemRequestDtoListMapperImpl;
import ru.practicum.shareit.request.mapping.impl.ToExtraItemRequestDtoMapperImpl;
import ru.practicum.shareit.request.mapping.impl.ToItemRequestDtoMapperImpl;
import ru.practicum.shareit.request.mapping.impl.ToItemRequestMapperImpl;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.impl.ItemRequestServiceImpl;
import ru.practicum.shareit.request.validators.NotBlankDescriptionOfItemRequestValidator;
import ru.practicum.shareit.request.validators.impl.NotBlankDescriptionOfItemRequestValidatorImpl;
import ru.practicum.shareit.user.mapping.impl.ToUserDtoMapperImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.ExternalUserService;

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
public class RequestServiceImplTest {

    private final TestDataGenerator testDataGenerator = new TestDataGenerator();

    private ItemRequestServiceImpl itemRequestService;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    private SharerUserValidator sharerUserValidator;
    private NotBlankDescriptionOfItemRequestValidator notBlankDescriptionOfItemRequestValidator;

    private ToItemRequestMapper toItemRequestMapper;
    private ToItemRequestDtoMapper toItemRequestDtoMapper;
    private ToExtraItemRequestDtoMapper toExtraItemRequestDtoMapper;
    private ToExtraItemRequestDtoListMapper toExtraItemRequestDtoListMapper;

    @Mock
    private ExternalUserService userService;

    @Mock
    private ExternalItemService itemService;

    private ItemRequest testItemRequest;
    private ItemRequestDto testItemRequestDto;

    @BeforeEach
    void setUp() {
        sharerUserValidator = new SharerUserValidatorImpl();
        notBlankDescriptionOfItemRequestValidator = new NotBlankDescriptionOfItemRequestValidatorImpl();

        toItemRequestMapper = new ToItemRequestMapperImpl();
        toItemRequestDtoMapper = new ToItemRequestDtoMapperImpl(new ToUserDtoMapperImpl());
        toExtraItemRequestDtoMapper = new ToExtraItemRequestDtoMapperImpl(toItemRequestDtoMapper,
                new ToItemDtoMapperImpl(new ToUserDtoMapperImpl(),
                        new ToCommentDtoMapperImpl()));
        toExtraItemRequestDtoListMapper = new ToExtraItemRequestDtoListMapperImpl(toExtraItemRequestDtoMapper);


        itemRequestService = new ItemRequestServiceImpl(itemRequestRepository,
                sharerUserValidator,
                notBlankDescriptionOfItemRequestValidator,
                toItemRequestMapper,
                toItemRequestDtoMapper,
                toExtraItemRequestDtoMapper,
                toExtraItemRequestDtoListMapper,
                userService,
                itemService
        );

        testItemRequest = testDataGenerator.generateItemRequest();
        testItemRequestDto = testDataGenerator.generateItemRequestDto();
    }

    @Test
    public void createTest() {

        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(testItemRequest);

        User testUser = testDataGenerator.generateUser();
        when(userService.retrieve(any())).thenReturn(testUser);

        testItemRequestDto.setId(null);

        CreateItemRequestContext testContext = CreateItemRequestContext.builder()
                .createItemRequestDto(testDataGenerator.generateCreateItemRequestDto())
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
    public void retrieveForUserTest() {

        User testUser = testDataGenerator.generateUser();
        testUser.setId(1L);
        when(userService.retrieve(any())).thenReturn(testUser);

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
        User testUser = testDataGenerator.generateUser();
        testUser.setId(1L);
        when(userService.retrieve(any())).thenReturn(testUser);

        //Тестовый набор чужих запросов на предоставление вещей
        List<ItemRequest> repositoryResult = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ItemRequest itemRequest = testDataGenerator.generateItemRequest();
            repositoryResult.add(itemRequest);
        }
        Page<ItemRequest> pageRepositoryResult = new PageImpl<>(repositoryResult);
        when(itemRequestRepository.findAll(anyLong(), any(Pageable.class)))
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
                .findAll(anyLong(), any(Pageable.class));
    }

    @Test
    public void retrieve() {
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.ofNullable(testItemRequest));
        when(userService.retrieve(anyLong())).thenReturn(testDataGenerator.generateUser());

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

    @Test
    public void retrieveForExternal() {
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.ofNullable(testItemRequest));

        ItemRequest testResult = itemRequestService.retrieve(1L);

        assertNotNull(testResult, "Не возвращается результат создания записи.");
        assertThat(testResult.getId(), equalTo(testItemRequest.getId()));
        Mockito.verify(itemRequestRepository, Mockito.times(1))
                .findById(anyLong());
    }
}
