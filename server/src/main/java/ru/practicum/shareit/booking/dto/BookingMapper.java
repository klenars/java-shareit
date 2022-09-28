package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.model.Booking;

import java.util.List;
import java.util.stream.Collectors;

public class BookingMapper {

    public static Booking mapDtoInToBooker(BookingRequestDto bookingRequestDto) {
        Booking booking = new Booking();
        booking.setStart(bookingRequestDto.getStart());
        booking.setEnd(bookingRequestDto.getEnd());

        return booking;
    }

    public static BookingDtoForItem mapBookingToDtoForItem(Booking booking) {
        if (booking == null) {
            return null;
        }
        BookingDtoForItem bookingDtoForItem = new BookingDtoForItem();
        bookingDtoForItem.setId(booking.getId());
        bookingDtoForItem.setBookerId(booking.getBooker().getId());

        return bookingDtoForItem;
    }

    public static BookingResponseDto mapBookingToResponseDto(Booking booking) {
        return new BookingResponseDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getStatus(),
                booking.getBooker(),
                booking.getItem()
        );
    }

    public static List<BookingResponseDto> mapListBookingToListResponseDto(List<Booking> bookings) {
        return bookings.stream()
                .map(BookingMapper::mapBookingToResponseDto)
                .collect(Collectors.toList());
    }
}
