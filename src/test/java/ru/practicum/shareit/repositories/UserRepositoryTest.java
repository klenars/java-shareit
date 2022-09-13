package ru.practicum.shareit.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.exception.UserDoesntExistException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    private User user = new User(
            0,
            "User",
            "user@mail.ru"
    );

    @BeforeEach
    void beforeEach() {
        user = userRepository.save(user);
    }

    @Test
    void checkUserExist() {
        Assertions.assertDoesNotThrow(() -> userRepository.checkUserExist(user.getId()));
        Assertions.assertThrows(UserDoesntExistException.class, () -> userRepository.checkUserExist(888));
    }

    @Test
    void getById() {
        User requestUser = userRepository.getById(user.getId());

        assertThat(requestUser.getId(), equalTo(user.getId()));
        assertThat(requestUser.getName(), equalTo(user.getName()));
        assertThat(requestUser.getEmail(), equalTo(user.getEmail()));

        Assertions.assertThrows(UserDoesntExistException.class, () -> userRepository.getById(888));
    }
}