package ru.practicum.shareit.user;

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
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.contexts.DeleteUserContext;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    private final TestDataGenerator testDataGenerator = new TestDataGenerator();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserDto testUserDto;

    @BeforeEach
    public void setUp() {
        testUserDto = testDataGenerator.generateUserDto();
    }

    @SneakyThrows
    @Test
    public void createRequestTest() {
        when(userService.create(
                argThat(argument -> argument.getUserDto().equals(testUserDto)))).thenReturn(testUserDto);

        testUserDto.setId(null);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(testUserDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.name").value(testUserDto.getName()))
                .andExpect(jsonPath("$.email").value(testUserDto.getEmail()));

        verify(userService).create(argThat(argument -> argument.getUserDto().equals(testUserDto)));
        verifyNoMoreInteractions(userService);
    }

    @SneakyThrows
    @Test
    public void updateRequestTest() {
        when(userService.update(argThat(argument -> argument.getUserDto().equals(testUserDto)))).thenReturn(testUserDto);

        testUserDto.setId(null);

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(testUserDto))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.name").value(testUserDto.getName()))
                .andExpect(jsonPath("$.email").value(testUserDto.getEmail()));

        verify(userService).update(argThat(argument -> argument.getUserDto().equals(testUserDto)));
        verifyNoMoreInteractions(userService);
    }

    @SneakyThrows
    @Test
    public void retrieveOneRequestTest() {
        when(userService.retrieve(
                argThat(argument -> argument.getTargetUserId().equals(testUserDto.getId())))).thenReturn(testUserDto);

        mockMvc.perform(get("/users/{userId}", testUserDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.name").value(testUserDto.getName()))
                .andExpect(jsonPath("$.email").value(testUserDto.getEmail()));

        verify(userService).retrieve(
                argThat(argument -> argument.getTargetUserId().equals(testUserDto.getId())));
        verifyNoMoreInteractions(userService);
    }

    @SneakyThrows
    @Test
    public void retrieveAllRequestTest() {
        when(userService.retrieve()).thenReturn(List.of(testUserDto));

        mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(testUserDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name").value(testUserDto.getName()))
                .andExpect(jsonPath("$[0].email").value(testUserDto.getEmail()));

        verify(userService).retrieve();
        verifyNoMoreInteractions(userService);
    }

    @SneakyThrows
    @Test
    public void deleteRequestTest() {
        mockMvc.perform(delete("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(userService).delete(argThat(argument -> argument.getTargetUserId().equals(1L)));
        verifyNoMoreInteractions(userService);
    }

    @SneakyThrows
    @Test
    public void deleteByWrongIdRequestTest() {

        doThrow(new NotFoundException("not found")).when(userService).delete(any(DeleteUserContext.class));

        mockMvc.perform(delete("/users/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(userService).delete(argThat(argument -> argument.getTargetUserId().equals(99L)));
        verifyNoMoreInteractions(userService);
    }

}
