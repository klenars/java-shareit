package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
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
    public BookingResponseDto createBooking(long userId, BookingRequestDto bookingRequestDto) {
        userRepository.checkUserExist(userId);
        User booker = userRepository.getReferenceById(userId);
        Item item = itemRepository.getReferenceById(bookingRequestDto.getItemId());
        checkAuthorIsNotOwner(userId, item);
        checkItemAvailable(item);
        Booking booking = BookingMapper.mapDtoInToBooker(bookingRequestDto);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(BookingStatus.WAITING);

        return BookingMapper.mapBookingToResponseDto(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDto approveBooking(long userId, long bookingId, boolean approve) {
        userRepository.checkUserExist(userId);
        Booking booking = bookingRepository.getReferenceById(bookingId);
        checkBookingStatus(booking);
        Item item = booking.getItem();
        itemRepository.checkItemsOwner(userId, item);

        booking.setStatus(approve ? BookingStatus.APPROVED : BookingStatus.REJECTED);

        return BookingMapper.mapBookingToResponseDto(bookingRepository.save(booking));
    }

    @Override
    public BookingResponseDto getBookingById(long userId, long bookingId) {
        Booking booking = bookingRepository.getReferenceById(bookingId);
        bookingRepository.checkAuthorBookingOrOwnerItem(userId, booking);

        return BookingMapper.mapBookingToResponseDto(booking);
    }

    @Override
    public List<BookingResponseDto> getBookingByUser(long userId, BookingStateIn state, int from, int size) {
        userRepository.checkUserExist(userId);

        List<Booking> currentList;

        switch (state) {
            case CURRENT:
                currentList = bookingRepository.findByBooker_IdAndStartBeforeAndEndAfter(userId, LocalDateTime.now());
                break;
            case PAST:
                currentList = bookingRepository.findByBooker_IdAndEndBefore(userId, LocalDateTime.now());
                break;
            case FUTURE:
                currentList = bookingRepository.findByBooker_IdAndStartAfter(userId, LocalDateTime.now());
                break;
            case WAITING:
                currentList = bookingRepository.findByBooker_IdAndStatus(userId, BookingStatus.WAITING);
                break;
            case REJECTED:
                currentList = bookingRepository.findByBooker_IdAndStatus(userId, BookingStatus.REJECTED);
                break;
            default:
                currentList = bookingRepository.getAllByAuthor(userId, PageRequest.of(getPageNumber(from, size), size));
        }
        return BookingMapper.mapListBookingToListResponseDto(currentList);
    }

    @Override
    public List<BookingResponseDto> getBookingByOwner(long ownerId, BookingStateIn state, int from, int size) {
        userRepository.checkUserExist(ownerId);

        List<Booking> currentList;

        switch (state) {
            case CURRENT:
                currentList = bookingRepository.findByItem_Owner_IdAndStartBeforeAndEndAfter(ownerId, LocalDateTime.now());
                break;
            case PAST:
                currentList = bookingRepository.findByItem_Owner_IdAndEndBefore(ownerId, LocalDateTime.now());
                break;
            case FUTURE:
                currentList = bookingRepository.findByItem_Owner_IdAndStartAfter(ownerId, LocalDateTime.now());
                break;
            case WAITING:
                currentList = bookingRepository.findByItem_Owner_IdAndStatus(ownerId, BookingStatus.WAITING);
                break;
            case REJECTED:
                currentList = bookingRepository.findByItem_Owner_IdAndStatus(ownerId, BookingStatus.REJECTED);
                break;
            default:
                currentList = bookingRepository.findByItem_Owner_Id(ownerId, PageRequest.of(getPageNumber(from, size), size));
        }
        return BookingMapper.mapListBookingToListResponseDto(currentList);
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

    private int getPageNumber(int from, int size) {
        return from / size;
    }
}
