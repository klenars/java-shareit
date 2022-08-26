package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingStateIn;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.FieldValidationException;
import ru.practicum.shareit.exception.UserValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public Booking createBooking(long userId, BookingDtoIn bookingDtoIn) {
        userRepository.checkUserExist(userId);
        checkDateStartAndEnd(bookingDtoIn);
        User booker = userRepository.getReferenceById(userId);
        Item item = itemRepository.getReferenceById(bookingDtoIn.getItemId());
        checkAuthorIsNotOwner(userId, item);
        checkItemAvailable(item);
        Booking booking = BookingMapper.mapDtoInToBooker(bookingDtoIn);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);

        return bookingRepository.save(booking);
    }

    @Override
    public Booking approveBooking(long userId, long bookingId, boolean approve) {
        userRepository.checkUserExist(userId);
        Booking booking = bookingRepository.getReferenceById(bookingId);
        checkBookingStatus(booking);
        Item item = booking.getItem();
        itemRepository.checkItemsOwner(userId, item);

        booking.setStatus(approve ? BookingStatus.APPROVED : BookingStatus.REJECTED);

        return bookingRepository.save(booking);
    }

    @Override
    public Booking getBookingById(long userId, long bookingId) {
        Booking booking = bookingRepository.getReferenceById(bookingId);
        bookingRepository.checkAuthorBookingOrOwnerItem(userId, booking);

        return booking;
    }

    @Override
    public List<Booking> getBookingByUser(long userId, BookingStateIn state) {
        userRepository.checkUserExist(userId);

        switch (state) {
            case CURRENT:
                return bookingRepository.findByBooker_IdAndStartBeforeAndEndAfter(userId, LocalDateTime.now());
            case PAST:
                return bookingRepository.findByBooker_IdAndEndBefore(userId, LocalDateTime.now());
            case FUTURE:
                return bookingRepository.findByBooker_IdAndStartAfter(userId, LocalDateTime.now());
            case WAITING:
                return bookingRepository.findByBooker_IdAndStatus(userId, BookingStatus.WAITING);
            case REJECTED:
                return bookingRepository.findByBooker_IdAndStatus(userId, BookingStatus.REJECTED);
            default:
                return bookingRepository.getAllByAuthor(userId);
        }
    }

    @Override
    public List<Booking> getBookingByOwner(long ownerId, BookingStateIn state) {
        userRepository.checkUserExist(ownerId);

        switch (state) {
            case CURRENT:
                return bookingRepository.findByItem_Owner_IdAndStartBeforeAndEndAfter(ownerId, LocalDateTime.now());
            case PAST:
                return bookingRepository.findByItem_Owner_IdAndEndBefore(ownerId, LocalDateTime.now());
            case FUTURE:
                return bookingRepository.findByItem_Owner_IdAndStartAfter(ownerId, LocalDateTime.now());
            case WAITING:
                return bookingRepository.findByItem_Owner_IdAndStatus(ownerId, BookingStatus.WAITING);
            case REJECTED:
                return bookingRepository.findByItem_Owner_IdAndStatus(ownerId, BookingStatus.REJECTED);
            default:
                return bookingRepository.findByItem_Owner_Id(ownerId);
        }
    }

    private void checkBookingStatus(Booking booking) {
        if (booking.getStatus() != BookingStatus.WAITING) {
            throw new FieldValidationException(
                    String.format("Booking id=%d already has status %S!", booking.getId(), booking.getStatus())
            );
        }
    }

    private void checkAuthorIsNotOwner(long userId, Item item) {
        if (userId == item.getOwner().getId()) {
            throw new UserValidationException(
                    String.format("Booking create FAIL! User id=%d is owner of Item id=%d", userId, item.getId())
            );
        }
    }

    private void checkItemAvailable(Item item) {
        if (!item.getAvailable()) {
            throw new FieldValidationException(
                    String.format("Item id=%d isn't available for booking!", item.getId())
            );
        }
    }

    private void checkDateStartAndEnd(BookingDtoIn bookingDtoIn) {
        String errorMessage = null;

        if (bookingDtoIn.getStart().isBefore(LocalDateTime.now())) {
            errorMessage = "Start booking can't be in the past!";
        } else if (bookingDtoIn.getEnd().isBefore(LocalDateTime.now())) {
            errorMessage = "End booking can't be in the past!";
        } else if (bookingDtoIn.getEnd().isBefore(bookingDtoIn.getStart())) {
            errorMessage = "End booking can't be earlier than start!";
        }

        if (errorMessage != null) {
            throw new FieldValidationException(errorMessage);
        }
    }
}
