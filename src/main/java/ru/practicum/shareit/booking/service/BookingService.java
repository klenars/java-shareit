package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingStateIn;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {
    Booking createBooking(long userId, BookingDtoIn bookingDtoIn);

    Booking approveBooking(long userId, long bookingId, boolean approve);

    Booking getBookingById(long userId, long bookingId);

    List<Booking> getBookingByUser(long userId, BookingStateIn state);

    List<Booking> getBookingByOwner(long ownerId, BookingStateIn state
    );
}
