package ru.practicum.shareit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    BookingService bookingService;

    @Autowired
    private MockMvc mockMvc;

    private final BookingResponseDto bookingResponseDto = new BookingResponseDto(
            888L,
            LocalDateTime.of(2022, 2, 22, 22, 22, 22),
            LocalDateTime.of(2023, 3, 3, 3, 3, 3),
            BookingStatus.APPROVED,
            null,
            null
    );

    @Test
    void createBooking() throws Exception {
        Mockito
                .when(bookingService.createBooking(anyLong(), any()))
                .thenReturn(bookingResponseDto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 888)
                        .content(objectMapper.writeValueAsString(bookingResponseDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id", is(bookingResponseDto.getId()), Long.class),
                        jsonPath("$.start", is(bookingResponseDto.getStart().toString())),
                        jsonPath("$.end", is(bookingResponseDto.getEnd().toString())),
                        jsonPath("$.status", is(bookingResponseDto.getStatus().toString()))
                );
    }

    @Test
    void approveBooking() throws Exception{
        Mockito
                .when(bookingService.approveBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingResponseDto);

        mockMvc.perform(patch("/bookings/888")
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id", is(bookingResponseDto.getId()), Long.class),
                        jsonPath("$.start", is(bookingResponseDto.getStart().toString())),
                        jsonPath("$.end", is(bookingResponseDto.getEnd().toString())),
                        jsonPath("$.status", is(bookingResponseDto.getStatus().toString()))
                );
    }

    @Test
    void getBookingById() throws Exception{
        Mockito
                .when(bookingService.getBookingById(anyLong(), anyLong()))
                .thenReturn(bookingResponseDto);

        mockMvc.perform(get("/bookings/888")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id", is(bookingResponseDto.getId()), Long.class),
                        jsonPath("$.start", is(bookingResponseDto.getStart().toString())),
                        jsonPath("$.end", is(bookingResponseDto.getEnd().toString())),
                        jsonPath("$.status", is(bookingResponseDto.getStatus().toString()))
                );
    }

    @Test
    void getBookingByUser() throws Exception{
        Mockito
                .when(bookingService.getBookingByUser(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingResponseDto));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().json(objectMapper.writeValueAsString(List.of(bookingResponseDto)))
                );
    }

    @Test
    void getBookingByOwner() throws Exception{
        Mockito
                .when(bookingService.getBookingByOwner(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingResponseDto));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().json(objectMapper.writeValueAsString(List.of(bookingResponseDto)))
                );
    }
}