package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.model.Booking;

public interface BookingService {
    Booking createBooking(long userId, BookingDtoIn bookingDtoIn);

    Booking approveBooking(long userId, long bookingId, boolean approve);

    Booking getBookingById(long userId, long bookingId);
}
