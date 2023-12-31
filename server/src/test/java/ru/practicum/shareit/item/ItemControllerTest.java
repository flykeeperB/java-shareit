package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.TestDataGenerator.TestDataGenerator;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.item.contexts.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemExtraDto;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {

    private final TestDataGenerator testDataGenerator = new TestDataGenerator();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    private ItemDto testItemDto;
    private ItemExtraDto testItemExtraDto;

    @BeforeEach
    public void setUp() {
        testItemDto = testDataGenerator.generateItemDto();
        testItemExtraDto = testDataGenerator.generateItemExtraDto();
    }

    @SneakyThrows
    @Test
    public void createRequestTest() {
        when(itemService.create(argThat(argument -> argument.getItemDto().equals(testItemDto))))
                .thenReturn(testItemDto);

        testItemDto.setId(null);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(testItemDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testItemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name").value(testItemDto.getName()))
                .andExpect(jsonPath("$.description").value(testItemDto.getDescription()));

        verify(itemService).create(argThat(argument -> argument.getItemDto().equals(testItemDto)));
        verifyNoMoreInteractions(itemService);
    }

    @SneakyThrows
    @Test
    public void updateRequestTest() {
        when(itemService.update(argThat(argument -> argument.getItemDto().equals(testItemDto))))
                .thenReturn(testItemDto);

        testItemDto.setId(null);

        mockMvc.perform(MockMvcRequestBuilders.patch("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(testItemDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testItemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name").value(testItemDto.getName()))
                .andExpect(jsonPath("$.description").value(testItemDto.getDescription()));

        verify(itemService).update(
                argThat(argument -> argument.getItemDto().equals(testItemDto)));
        verifyNoMoreInteractions(itemService);
    }

    @SneakyThrows
    @Test
    public void updateItemByWrongUserRequestTest() {
        doThrow(new AccessDeniedException("обновить данные может только владелец вещи"))
                .when(itemService).update(argThat(argument -> argument.getSharerUserId().equals(99L)));

        mockMvc.perform(MockMvcRequestBuilders.patch("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 99L)
                        .content(objectMapper.writeValueAsString(testItemDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verify(itemService).update(argThat(argument -> argument.getSharerUserId().equals(99L)));
        verifyNoMoreInteractions(itemService);
    }

    @SneakyThrows
    @Test
    public void deleteRequestTest() {
        mockMvc.perform(delete("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(itemService).delete(argThat(argument -> argument.getTargetItemId().equals(1L)));
        verifyNoMoreInteractions(itemService);
    }

    @SneakyThrows
    @Test
    public void retrieveOneRequestTest() {
        when(itemService.retrieve(argThat(argument -> argument.getTargetItemId().equals(1L))))
                .thenReturn(testItemExtraDto);

        mockMvc.perform(get("/items/{itemId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testItemExtraDto.getId()), Long.class))
                .andExpect(jsonPath("$.name").value(testItemExtraDto.getName()))
                .andExpect(jsonPath("$.description").value(testItemExtraDto.getDescription()));

        verify(itemService).retrieve(argThat(argument -> argument.getTargetItemId().equals(1L)));
        verifyNoMoreInteractions(itemService);
    }

    @SneakyThrows
    @Test
    public void retrieveForOwnerRequestTest() {
        when(itemService.retrieveForOwner(argThat(argument -> argument.getSharerUserId().equals(1L))))
                .thenReturn(List.of(testItemExtraDto));

        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "1")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(testItemExtraDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name").value(testItemExtraDto.getName()))
                .andExpect(jsonPath("$[0].description").value(testItemExtraDto.getDescription()));

        verify(itemService).retrieveForOwner(argThat(argument -> argument.getSharerUserId().equals(1L)));
        verifyNoMoreInteractions(itemService);
    }

    @SneakyThrows
    @Test
    public void retrieveAvailableForSearchTextTest() {
        when(itemService.retrieveAvailableForSearchText(any(RetrieveAvailableForSearchTextContext.class)))
                .thenReturn(List.of(testItemExtraDto));

        mockMvc.perform(get("/items/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "1")
                        .param("size", "10")
                        .param("text", testItemExtraDto.getName())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(testItemExtraDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name").value(testItemExtraDto.getName()))
                .andExpect(jsonPath("$[0].description").value(testItemExtraDto.getDescription()));

        verify(itemService)
                .retrieveAvailableForSearchText(
                        argThat(argument -> argument.getSearchText().equals(testItemExtraDto.getName())));
        verifyNoMoreInteractions(itemService);
    }

    @SneakyThrows
    @Test
    public void createCommentTest() {
        CommentDto testOnlyTextCommentDto = testDataGenerator.generateOnlyTextCommentDto();
        CommentDto testDetailedCommentDto = testDataGenerator.generateDetailedCommentDto();
        testDetailedCommentDto.setText(testOnlyTextCommentDto.getText());

        when(itemService.createComment(any(CreateCommentContext.class)))
                .thenReturn(testDetailedCommentDto);


        mockMvc.perform(post("/items/1/comment")
                        .content(objectMapper.writeValueAsString(testOnlyTextCommentDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testDetailedCommentDto.getId()), Long.class))
                .andExpect(jsonPath("$.authorName").value(testDetailedCommentDto.getAuthorName()))
                .andExpect(jsonPath("$.text").value(testDetailedCommentDto.getText()));

        verify(itemService, times(1))
                .createComment(
                        argThat(argument -> argument.getComment().getText().equals(testOnlyTextCommentDto.getText())));
    }

}
