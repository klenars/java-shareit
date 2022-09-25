package ru.practicum.shareit.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.exception.UserValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    private User user = new User(
            0,
            "Ivan Ivanov",
            "Ivan@yandex.ru"
    );

    private ItemRequest itemRequest = new ItemRequest(
            0,
            "Request",
            user,
            LocalDateTime.now()
    );

    private Item item = new Item(
            0,
            "Otvertka",
            "Good Otvertka",
            true,
            user,
            itemRequest
    );

    @BeforeEach
    void beforeEach() {
        user = userRepository.save(user);
        itemRequest = requestRepository.save(itemRequest);
        item = itemRepository.save(item);
    }

    @Test
    void findByOwnerId() {
        List<Item> items = itemRepository.findByOwnerId(user.getId(), PageRequest.of(0, 10));

        assertThat(items, equalTo(List.of(item)));
    }

    @Test
    void findBySubstring() {
        List<Item> items = itemRepository.findBySubstring(item.getName(), PageRequest.of(0, 10));

        assertThat(items, equalTo(List.of(item)));
    }

    @Test
    void findByRequestId() {
        List<Item> items = itemRepository.findByRequestId(itemRequest.getId());

        assertThat(items, equalTo(List.of(item)));
    }

    @Test
    void checkItemsOwner() {
        assertDoesNotThrow(() -> itemRepository.checkItemsOwner(user.getId(), item));
        assertThrows(UserValidationException.class, () -> itemRepository.checkItemsOwner(888, item));
    }
}