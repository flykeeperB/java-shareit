package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.TestDataGenerator.TestDataGenerator;
import ru.practicum.shareit.request.contexts.CreateItemRequestContext;
import ru.practicum.shareit.request.contexts.RetrieveItemRequestContext;
import ru.practicum.shareit.request.contexts.RetrieveItemRequestsContext;
import ru.practicum.shareit.request.contexts.RetrieveItemRequestsForUserContext;
import ru.practicum.shareit.request.dto.ExtraItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ControllerItemRequestService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = ItemRequestController.class)
public class RequestControllerTest {

    private final TestDataGenerator testDataGenerator = new TestDataGenerator();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ControllerItemRequestService itemRequestService;

    private ItemRequest testItemRequest;
    private ItemRequestDto testItemRequestDto;
    private ExtraItemRequestDto testExtraItemRequestDto;

    @BeforeEach
    public void setUp() {
        testItemRequest = testDataGenerator.generateItemRequest();
        testItemRequestDto = testDataGenerator.generateItemRequestDto();
        testExtraItemRequestDto = testDataGenerator.generateExtraItemRequestDto();
    }

    @SneakyThrows
    @Test
    public void createRequestTest() {
        when(itemRequestService.create(any(CreateItemRequestContext.class))).thenReturn(testItemRequestDto);

        mockMvc.perform(post("/requests")
                        .content(objectMapper
                                .writeValueAsString(testDataGenerator.generateItemRequestWithOnlyTextDto()))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testItemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description").value(testItemRequestDto.getDescription()));

        verify(itemRequestService).create(any(CreateItemRequestContext.class));
        verifyNoMoreInteractions(itemRequestService);
    }

    @SneakyThrows
    @Test
    public void retrieveRequestsOfUserTest() {
        when(itemRequestService.retrieve(any(RetrieveItemRequestsForUserContext.class)))
                .thenReturn(List.of(testExtraItemRequestDto));

        mockMvc.perform(get("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(testExtraItemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description").value(testExtraItemRequestDto.getDescription()));

        verify(itemRequestService).retrieve(any(RetrieveItemRequestsForUserContext.class));
        verifyNoMoreInteractions(itemRequestService);
    }

    @SneakyThrows
    @Test
    public void retrieveAllRequestsTest() {
        when(itemRequestService.retrieve(any(RetrieveItemRequestsContext.class)))
                .thenReturn(List.of(testExtraItemRequestDto));

        mockMvc.perform(get("/requests/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "1")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(testExtraItemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description").value(testExtraItemRequestDto.getDescription()));

        verify(itemRequestService).retrieve(any(RetrieveItemRequestsContext.class));
        verifyNoMoreInteractions(itemRequestService);
    }

    @SneakyThrows
    @Test
    public void retrieveOneRequestTest() {
        when(itemRequestService.retrieve(any(RetrieveItemRequestContext.class))).thenReturn(testExtraItemRequestDto);

        mockMvc.perform(get("/requests/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testExtraItemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description").value(testExtraItemRequestDto.getDescription()));

        verify(itemRequestService).retrieve(any(RetrieveItemRequestContext.class));
        verifyNoMoreInteractions(itemRequestService);
    }


}
