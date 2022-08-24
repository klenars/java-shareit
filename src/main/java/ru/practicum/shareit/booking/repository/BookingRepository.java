package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.UserValidationException;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    default void checkAuthorBookingOrOwnerItem(long userId, Booking booking) {
        if (booking.getBooker().getId() != userId && userId != booking.getItem().getOwner().getId()) {
            throw new UserValidationException(
                    String.format(
                            "User id=%d isn't author of booking id=%d or owner of Item id=%d!",
                            userId,
                            booking.getId(),
                            booking.getItem().getOwner().getId()
                    )
            );
        }
    }
}
