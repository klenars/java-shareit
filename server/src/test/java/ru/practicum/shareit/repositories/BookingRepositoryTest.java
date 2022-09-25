package ru.practicum.shareit.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.UserValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User user = new User(
            0,
            "Ivan Ivanov",
            "Ivan@yandex.ru"
    );

    private Item item = new Item(
            0,
            "Otvertka",
            "Good Otvertka",
            true,
            user,
            null
    );

    private Booking booking = new Booking(
            0,
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(2),
            item,
            user,
            BookingStatus.WAITING
    );

    @BeforeEach
    void beforeEach() {
        user = userRepository.save(user);
        item = itemRepository.save(item);
        booking = bookingRepository.save(booking);
    }

    @Test
    void getAllByAuthor() {
        List<Booking> bookings = bookingRepository.getAllByAuthor(user.getId(), PageRequest.of(0, 10));

        assertThat(bookings, equalTo(List.of(booking)));
    }

    @Test
    void findByBooker_IdAndEndBefore() {
        List<Booking> bookings = bookingRepository.findByBooker_IdAndEndBefore(user.getId(), LocalDateTime.now());
        assertThat(bookings, equalTo(Collections.emptyList()));

        booking.setEnd(LocalDateTime.now().minusDays(3));
        bookingRepository.save(booking);

        List<Booking> bookings1 = bookingRepository.findByBooker_IdAndEndBefore(user.getId(), LocalDateTime.now());
        assertThat(bookings1, equalTo(List.of(booking)));
    }

    @Test
    void findByBooker_IdAndStartAfter() {
        List<Booking> bookings = bookingRepository.findByBooker_IdAndStartAfter(user.getId(), LocalDateTime.now());
        assertThat(bookings, equalTo(List.of(booking)));
    }

    @Test
    void findByBooker_IdAndStatus() {
        List<Booking> bookings = bookingRepository.findByBooker_IdAndStatus(user.getId(), BookingStatus.WAITING);
        assertThat(bookings, equalTo(List.of(booking)));
    }

    @Test
    void findByBooker_IdAndStartBeforeAndEndAfter() {
        booking.setStart(LocalDateTime.now().minusDays(3));
        bookingRepository.save(booking);

        List<Booking> bookings1 = bookingRepository.findByBooker_IdAndStartBeforeAndEndAfter(
                user.getId(),
                LocalDateTime.now()
        );
        assertThat(bookings1, equalTo(List.of(booking)));
    }

    @Test
    void findByItem_Owner_Id() {
        List<Booking> bookings = bookingRepository.findByItem_Owner_Id(user.getId(), PageRequest.of(0, 10));
        assertThat(bookings, equalTo(List.of(booking)));
    }

    @Test
    void findByItem_Owner_IdAndEndBefore() {
        booking.setEnd(LocalDateTime.now().minusDays(3));
        bookingRepository.save(booking);

        List<Booking> bookings = bookingRepository.findByItem_Owner_IdAndEndBefore(user.getId(), LocalDateTime.now());
        assertThat(bookings, equalTo(List.of(booking)));
    }

    @Test
    void findByItem_Owner_IdAndStartAfter() {
        List<Booking> bookings = bookingRepository.findByItem_Owner_IdAndStartAfter(user.getId(), LocalDateTime.now());
        assertThat(bookings, equalTo(List.of(booking)));
    }

    @Test
    void findByItem_Owner_IdAndStartBeforeAndEndAfter() {
        booking.setStart(LocalDateTime.now().minusDays(3));
        bookingRepository.save(booking);

        List<Booking> bookings1 = bookingRepository.findByItem_Owner_IdAndStartBeforeAndEndAfter(
                user.getId(),
                LocalDateTime.now()
        );
        assertThat(bookings1, equalTo(List.of(booking)));
    }

    @Test
    void findByItem_Owner_IdAndStatus() {
        List<Booking> bookings = bookingRepository.findByItem_Owner_IdAndStatus(user.getId(), BookingStatus.WAITING);
        assertThat(bookings, equalTo(List.of(booking)));
    }

    @Test
    void findByItem_IdAndEndBeforeOrderByEndDesc() {
        booking.setEnd(LocalDateTime.now().minusDays(3));
        bookingRepository.save(booking);

        Booking requestBooking = bookingRepository.findByItem_IdAndEndBeforeOrderByEndDesc(
                item.getId(),
                LocalDateTime.now()
        );
        assertThat(requestBooking, equalTo(booking));
    }

    @Test
    void findByItem_IdAndStartAfterOrderByStartAsc() {
        Booking requestBooking = bookingRepository.findByItem_IdAndStartAfterOrderByStartAsc(
                item.getId(),
                LocalDateTime.now()
        ).stream().findFirst().orElse(null);
        assertThat(requestBooking, equalTo(booking));
    }

    @Test
    void existsByItem_IdAndBooker_IdAndEndBefore() {
        booking.setEnd(LocalDateTime.now().minusDays(3));
        bookingRepository.save(booking);

        boolean request = bookingRepository.existsByItem_IdAndBooker_IdAndEndBefore(
                item.getId(), user.getId(), LocalDateTime.now()
        );

        assertTrue(request);

        boolean requestFalse = bookingRepository.existsByItem_IdAndBooker_IdAndEndBefore(
                item.getId(), 888, LocalDateTime.now()
        );

        assertFalse(requestFalse);
    }

    @Test
    void checkAuthorBookingOrOwnerItem() {
        assertDoesNotThrow(() -> bookingRepository.checkAuthorBookingOrOwnerItem(user.getId(), booking));
        assertThrows(
                UserValidationException.class,
                () -> bookingRepository.checkAuthorBookingOrOwnerItem(888, booking)
        );
    }
}