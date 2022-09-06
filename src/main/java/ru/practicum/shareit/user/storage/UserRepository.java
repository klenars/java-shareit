package ru.practicum.shareit.user.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.exception.UserDoesntExistException;
import ru.practicum.shareit.user.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    default void checkUserExist(long userId) {
        if (!existsById(userId)) {
            throw new UserDoesntExistException(String.format("User id=%d doesn't exist!", userId));
        }
    }

    default User getById(long id) {
        return findById(id)
                .orElseThrow(() -> new UserDoesntExistException(String.format("User id=%d doesn't exist!", id)));
    }
}