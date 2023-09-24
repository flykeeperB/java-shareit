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
import ru.practicum.shareit.request.contexts.RetrieveItemRequestContext;
import ru.practicum.shareit.request.contexts.RetrieveItemRequestsContext;
import ru.practicum.shareit.request.contexts.RetrieveItemRequestsForUserContext;
import ru.practicum.shareit.request.dto.ExtraItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
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
    private ItemRequestService itemRequestService;

    private ItemRequestDto testItemRequestDto;
    private ExtraItemRequestDto testExtraItemRequestDto;

    @BeforeEach
    public void setUp() {
        testItemRequestDto = testDataGenerator.generateItemRequestDto();
        testExtraItemRequestDto = testDataGenerator.generateExtraItemRequestDto();
    }

    @SneakyThrows
    @Test
    public void createRequestTest() {
        ItemRequestDto itemRequestDto = testDataGenerator.generateItemRequestDto();
        when(itemRequestService.create(argThat(argument -> argument.getItemRequestDto()
                .getDescription().equals(itemRequestDto.getDescription())))).thenReturn(testItemRequestDto);

        mockMvc.perform(post("/requests")
                        .content(objectMapper
                                .writeValueAsString(itemRequestDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testItemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description").value(testItemRequestDto.getDescription()));

        verify(itemRequestService).create(
                argThat(argument -> argument.getItemRequestDto().equals(itemRequestDto)));
        verifyNoMoreInteractions(itemRequestService);
    }

    @SneakyThrows
    @Test
    public void retrieveRequestsOfUserTest() {
        when(itemRequestService.retrieve(
                argThat((RetrieveItemRequestsForUserContext argument) -> argument.getSharerUserId().equals(1L))))
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

        verify(itemRequestService).retrieve(
                argThat((RetrieveItemRequestsForUserContext argument) -> argument.getSharerUserId().equals(1L)));
        verifyNoMoreInteractions(itemRequestService);
    }

    @SneakyThrows
    @Test
    public void retrieveAllRequestsTest() {
        when(itemRequestService.retrieve(
                argThat((RetrieveItemRequestsContext argument) -> argument.getSharerUserId().equals(1L))))
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

        verify(itemRequestService).retrieve(
                argThat((RetrieveItemRequestsContext argument) -> argument.getSharerUserId().equals(1L))
        );
        verifyNoMoreInteractions(itemRequestService);
    }

    @SneakyThrows
    @Test
    public void retrieveOneRequestTest() {
        when(itemRequestService.retrieve(
                        argThat((RetrieveItemRequestContext argument) -> argument.getSharerUserId().equals(1L))
                )
        ).thenReturn(testExtraItemRequestDto);

        mockMvc.perform(get("/requests/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testExtraItemRequestDto.getId()), Long.class))
                .andExpect(jsonPath("$.description").value(testExtraItemRequestDto.getDescription()));

        verify(itemRequestService).retrieve(argThat((RetrieveItemRequestContext argument) -> argument.getSharerUserId().equals(1L)));
        verifyNoMoreInteractions(itemRequestService);
    }


}
