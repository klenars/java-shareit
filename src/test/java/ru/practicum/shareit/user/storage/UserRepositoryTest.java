package ru.practicum.shareit.user.storage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    private final User user = new User(
            0,
            "User",
            "user@mail.ru"
    );

    @Test
    void checkUserExist() {
        System.out.println(userRepository.save(user));
    }

    @Test
    void getById() {
    }
}