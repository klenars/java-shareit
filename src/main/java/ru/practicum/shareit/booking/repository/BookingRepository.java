package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.UserValidationException;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select b from Booking b where b.booker.id = ?1 order by b.start DESC")
    List<Booking> getAllByAuthor(long id);

    @Query("select b from Booking b where b.booker.id = ?1 and b.end < ?2 order by b.start DESC")
    List<Booking> findByBooker_IdAndEndBefore(long id, LocalDateTime end);

    @Query("select b from Booking b where b.booker.id = ?1 and b.start > ?2 order by b.start DESC")
    List<Booking> findByBooker_IdAndStartAfter(long id, LocalDateTime start);

    @Query("select b from Booking b where b.booker.id = ?1 and b.status = ?2 order by b.start DESC")
    List<Booking> findByBooker_IdAndStatus(long id, BookingStatus status);

    @Query("select b from Booking b where b.item.owner.id = ?1 order by b.start DESC")
    List<Booking> findByItem_Owner_Id(long id);

    @Query("select b from Booking b where b.item.owner.id = ?1 and b.end < ?2 order by b.start DESC")
    List<Booking> findByItem_Owner_IdAndEndBefore(long id, LocalDateTime end);

    @Query("select b from Booking b where b.item.owner.id = ?1 and b.start > ?2 order by b.start DESC")
    List<Booking> findByItem_Owner_IdAndStartAfter(long id, LocalDateTime start);

    @Query("select b from Booking b where b.item.owner.id = ?1 and b.status = ?2 order by b.start DESC")
    List<Booking> findByItem_Owner_IdAndStatus(long id, BookingStatus status);

    @Query("select b from Booking b where b.item.id = ?1 and b.end < ?2 order by b.end DESC")
    Booking findByItem_IdAndEndBeforeOrderByEndDesc(long id, LocalDateTime end);

    @Query("select b from Booking b where b.item.id = ?1 and b.start > ?2 order by b.start")
    Booking findByItem_IdAndStartAfterOrderByStartAsc(long id, LocalDateTime start);

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
