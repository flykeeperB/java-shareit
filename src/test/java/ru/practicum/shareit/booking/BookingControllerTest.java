package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.contexts.BasicBookingContext;
import ru.practicum.shareit.booking.contexts.ForStateBookingContext;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingExtraDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    private final TestDataGenerator testDataGenerator = new TestDataGenerator();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    private User testBooker;
    private BookingDto testBookingDto;
    private BookingExtraDto testBookingExtraDto;

    @BeforeEach
    public void setUp() {
        testBooker = testDataGenerator.generateUser();
        testBookingExtraDto = testDataGenerator.generateBookingDto();
        testBookingDto = testDataGenerator.generateBookingDto();
    }


    @SneakyThrows
    @Test
    public void createBookingRequestTest() {
        when(bookingService.create(argThat(argument -> argument.getBookingExtraDto().equals(testBookingDto))))
                .thenReturn(testBookingExtraDto);

        testBookingDto.setId(null);
        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(testBookingDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBookingExtraDto.getId()), Long.class))
                .andExpect(jsonPath("$.start").value(testBookingExtraDto
                        .getStart().toString().replaceAll("0+$", "")))
                .andExpect(jsonPath("$.end").value(testBookingExtraDto
                        .getEnd().toString().replaceAll("0+$", "")));

        verify(bookingService).create(argThat(argument -> argument.getBookingExtraDto().equals(testBookingDto)));
        verifyNoMoreInteractions(bookingService);
    }


    @SneakyThrows
    @Test
    public void approveBookingRequestTest() {
        testBookingExtraDto.setStatus(BookingStatus.APPROVED);

        when(bookingService.approve(argThat(argument -> argument.getIsApproved().equals(true))))
                .thenReturn(testBookingExtraDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/bookings/{bookingId}", testBookingDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", testBooker.getId())
                        .param("approved", "true")
                        .content(objectMapper.writeValueAsString(testBookingDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBookingExtraDto.getId()), Long.class))
                .andExpect(jsonPath("$.start").value(testBookingExtraDto
                        .getStart().toString().replaceAll("0+$", "")))
                .andExpect(jsonPath("$.end").value(testBookingExtraDto
                        .getEnd().toString().replaceAll("0+$", "")));

        verify(bookingService).approve(argThat(argument -> argument.getIsApproved().equals(true)));
        verifyNoMoreInteractions(bookingService);
    }

    @SneakyThrows
    @Test
    public void retrieveBookingRequestTest() {
        when(bookingService.retrieve(argThat(argument -> argument.getSharerUserId().equals(1L))))
                .thenReturn(testBookingExtraDto);

        mockMvc.perform(get("/bookings/{bookingId}", testBookingExtraDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(testBookingExtraDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBookingExtraDto.getId()), Long.class))
                .andExpect(jsonPath("$.start").value(testBookingExtraDto
                        .getStart().toString().replaceAll("0+$", "")))
                .andExpect(jsonPath("$.end").value(testBookingExtraDto
                        .getEnd().toString().replaceAll("0+$", "")));

        verify(bookingService, times(1)).retrieve(any(BasicBookingContext.class));
        verifyNoMoreInteractions(bookingService);
    }

    @SneakyThrows
    @Test
    public void retrieveForBookerRequestTest() {
        when(bookingService.retrieveForBooker(argThat(argument -> argument.getState().equals(State.ALL))))
                .thenReturn(List.of(testBookingExtraDto));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("state", "ALL")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(testBookingExtraDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].start").value(testBookingExtraDto
                        .getStart().toString().replaceAll("0+$", "")))
                .andExpect(jsonPath("$[0].end").value(testBookingExtraDto
                        .getEnd().toString().replaceAll("0+$", "")));

        verify(bookingService, times(1)).retrieveForBooker(any(ForStateBookingContext.class));
        verifyNoMoreInteractions(bookingService);
    }

    @SneakyThrows
    @Test
    public void retrieveForItemsOwnerRequestTest() {
        when(bookingService.retrieveForItemsOwner(argThat(argument -> argument.getState().equals(State.ALL))))
                .thenReturn(List.of(testBookingExtraDto));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("state", "ALL")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(testBookingExtraDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].start").value(testBookingExtraDto
                        .getStart().toString().replaceAll("0+$", "")))
                .andExpect(jsonPath("$[0].end").value(testBookingExtraDto
                        .getEnd().toString().replaceAll("0+$", "")));

        verify(bookingService, times(1)).retrieveForItemsOwner(any(ForStateBookingContext.class));
        verifyNoMoreInteractions(bookingService);
    }
}
