package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingStateIn;

import java.util.List;

public interface BookingService {
    BookingResponseDto createBooking(long userId, BookingRequestDto bookingRequestDto);

    BookingResponseDto approveBooking(long userId, long bookingId, boolean approve);

    BookingResponseDto getBookingById(long userId, long bookingId);

    List<BookingResponseDto> getBookingByUser(long userId, BookingStateIn state);

    List<BookingResponseDto> getBookingByOwner(long ownerId, BookingStateIn state
    );
}
