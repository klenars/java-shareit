package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingStateIn;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public Booking createBooking(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestBody BookingDtoIn bookingDtoIn
    ) {
        return bookingService.createBooking(userId, bookingDtoIn);
    }

    @PatchMapping("/{bookingId}")
    public Booking approveBooking(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long bookingId,
            @RequestParam boolean approved
    ) {
        return bookingService.approveBooking(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public Booking getBookingById(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long bookingId
    ) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<Booking> getBookingByUser(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(required = false, defaultValue = "ALL") BookingStateIn state
    ) {
        return bookingService.getBookingByUser(userId, state);
    }

    @GetMapping("/owner")
    public List<Booking> getBookingByOwner(
            @RequestHeader("X-Sharer-User-Id") long ownerId,
            @RequestParam(required = false, defaultValue = "ALL") BookingStateIn state
    ) {
        return bookingService.getBookingByOwner(ownerId, state);
    }
}
