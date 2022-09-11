package ru.practicum.shareit.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DataJpaTest
class RequestRepositoryTest {

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

    @BeforeEach
    void beforeEach() {
        user = userRepository.save(user);
        itemRequest = requestRepository.save(itemRequest);
    }

    @Test
    void getById() {
        ItemRequest request = requestRepository.getById(itemRequest.getId());

        assertThat(request, equalTo(itemRequest));
        assertThat(request.getId(), equalTo(itemRequest.getId()));
        assertThat(request.getDescription(), equalTo(itemRequest.getDescription()));
    }

    @Test
    void getAllByRequestorId() {
        List<ItemRequest> requests = requestRepository.getAllByRequestorId(user.getId());

        assertThat(requests, equalTo(List.of(itemRequest)));
    }

    @Test
    void findByRequestor_IdNotOrderByCreatedDesc() {
        List<ItemRequest> requests = requestRepository.findByRequestor_IdNotOrderByCreatedDesc(user.getId(), PageRequest.of(0, 10));

        assertThat(requests, equalTo(Collections.emptyList()));
    }
}