package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

  private final BookingRepository bookingRepository;
  private final UserRepository userRepository;
  private final ItemRepository itemRepository;

    @Override
    public Booking createBooking(long userId, BookingDtoIn bookingDtoIn) {
        userRepository.checkUserExist(userId);
        User booker = userRepository.getReferenceById(userId);
        Item item = itemRepository.getReferenceById(bookingDtoIn.getItemId());
        Booking booking = BookingMapper.mapDtoInToBooker(bookingDtoIn);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);

        booking = bookingRepository.save(booking);
        System.out.println(booking.toString());

        return booking;
    }

    @Override
    public Booking approveBooking(long userId, long bookingId, boolean approve) {
        userRepository.checkUserExist(userId);
        Booking booking = bookingRepository.getReferenceById(bookingId);
        Item item = booking.getItem();
        itemRepository.checkItemsOwner(userId, item);

        booking.setStatus(approve ? BookingStatus.APPROVED : BookingStatus.REJECTED);

        return bookingRepository.save(booking);
    }

    @Override
    public Booking getBookingById(long userId, long bookingId) {
        Booking booking = bookingRepository.getReferenceById(bookingId);
        System.out.println(booking.toString());
        return booking;
    }
}
