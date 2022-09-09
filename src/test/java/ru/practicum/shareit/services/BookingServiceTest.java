package ru.practicum.shareit.services;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingStateIn;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.UserValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceTest {

    private final BookingService bookingService;

    @MockBean
    private final BookingRepository bookingRepository;

    @MockBean
    private final UserRepository userRepository;

    @MockBean
    private final ItemRepository itemRepository;

    private final BookingRequestDto bookingRequestDto = new BookingRequestDto(
            888L,
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(2)
    );

    private final User user = new User(
            888L,
            "Ivan Ivanov",
            "Ivan@yandex.ru"
    );

    private final Item item = new Item(
            888L,
            "Otvertka",
            "Good Otvertka",
            true,
            user,
            null
    );

    private final Booking booking = new Booking(
            888L,
            bookingRequestDto.getStart(),
            bookingRequestDto.getEnd(),
            item,
            user,
            BookingStatus.WAITING
    );

    @Test
    void createBooking() {
        Mockito
                .when(itemRepository.getReferenceById(anyLong()))
                .thenReturn(item);
        Mockito
                .when(bookingRepository.save(any()))
                .thenReturn(booking);

        BookingResponseDto bookingResponseDto = bookingService.createBooking(1L, bookingRequestDto);

        assertThat(bookingResponseDto.getId(), equalTo(booking.getId()));
        assertThat(bookingResponseDto.getStart(), equalTo(booking.getStart()));
        assertThat(bookingResponseDto.getEnd(), equalTo(booking.getEnd()));
        assertThat(bookingResponseDto.getStatus(), equalTo(booking.getStatus()));
        assertThat(bookingResponseDto.getItem(), equalTo(booking.getItem()));
        assertThat(bookingResponseDto.getBooker(), equalTo(booking.getBooker()));

        Assertions.assertThrows(UserValidationException.class, () -> {
            bookingService.createBooking(888L, bookingRequestDto);
        });
    }

    @Test
    void approveBooking() {
        Mockito
                .when(bookingRepository.getReferenceById(anyLong()))
                .thenReturn(booking);
        Mockito
                .when(bookingRepository.save(any()))
                .thenReturn(booking);

        BookingResponseDto bookingResponseDto = bookingService.approveBooking(1L, booking.getId(), true);

        assertThat(bookingResponseDto.getId(), equalTo(booking.getId()));
        assertThat(bookingResponseDto.getStart(), equalTo(booking.getStart()));
        assertThat(bookingResponseDto.getEnd(), equalTo(booking.getEnd()));
        assertThat(bookingResponseDto.getStatus(), equalTo(booking.getStatus()));
        assertThat(bookingResponseDto.getItem(), equalTo(booking.getItem()));
        assertThat(bookingResponseDto.getBooker(), equalTo(booking.getBooker()));
    }

    @Test
    void getBookingById() {
        Mockito
                .when(bookingRepository.save(any()))
                .thenReturn(booking);
        Mockito
                .when(itemRepository.getReferenceById(anyLong()))
                .thenReturn(item);

        BookingResponseDto bookingResponseDto = bookingService.createBooking(1L, bookingRequestDto);

        assertThat(bookingResponseDto.getId(), equalTo(booking.getId()));
        assertThat(bookingResponseDto.getStart(), equalTo(booking.getStart()));
        assertThat(bookingResponseDto.getEnd(), equalTo(booking.getEnd()));
        assertThat(bookingResponseDto.getStatus(), equalTo(booking.getStatus()));
        assertThat(bookingResponseDto.getItem(), equalTo(booking.getItem()));
        assertThat(bookingResponseDto.getBooker(), equalTo(booking.getBooker()));
    }

    @Test
    void getBookingByUser() {
        Mockito
                .when(bookingRepository.findByBooker_IdAndStatus(anyLong(), any()))
                .thenReturn(List.of(booking));

        List<BookingResponseDto> bookings = bookingService.getBookingByUser(1, BookingStateIn.WAITING, 0, 10);

        assertThat(bookings.size(), equalTo(List.of(booking).size()));
    }

    @Test
    void getBookingByOwner() {
        Mockito
                .when(bookingRepository.findByItem_Owner_IdAndStatus(anyLong(), any()))
                .thenReturn(List.of(booking));

        List<BookingResponseDto> bookings = bookingService.getBookingByOwner(1, BookingStateIn.WAITING, 0, 10);

        assertThat(bookings.size(), equalTo(List.of(booking).size()));
    }
}