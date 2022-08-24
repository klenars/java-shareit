package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.exception.UserValidationException;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query("select i from Item i where i.owner.id = ?1")
    List<Item> findByOwnerId(long id);

    @Query("select i from Item i " +
            "where i.available = true " +
                "and (upper(i.name) like upper(concat('%', ?1, '%')) " +
                "or upper(i.description) like upper(concat('%', ?1, '%')))")
    List<Item> findBySubstring(String text);

    default void checkItemsOwner(long userId, Item item) {
        if (item.getOwner().getId() != userId) {
            throw new UserValidationException(
                    String.format("User id=%d isn't owner of Item id=%d!", userId, item.getId())
            );
        }
    }
}