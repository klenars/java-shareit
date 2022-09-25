package ru.practicum.shareit.services;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestDtoIn;
import ru.practicum.shareit.requests.dto.RequestMapper;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.RequestRepository;
import ru.practicum.shareit.requests.service.RequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RequestServiceTest {

    private final RequestService requestService;

    @MockBean
    private final RequestRepository requestRepository;

    @MockBean
    private final UserRepository userRepository;

    private final User user = new User(
            888L,
            "Ivan Ivanov",
            "Ivan@yandex.ru"
    );

    private final ItemRequest itemRequest = new ItemRequest(
            888,
            "Description",
            user,
            LocalDateTime.now()
    );

    private final ItemRequestDtoIn requestDtoIn = new ItemRequestDtoIn(
            itemRequest.getDescription(),
            user.getId()
    );

    @Test
    void createRequest() {
        Mockito
                .when(requestRepository.save(any()))
                .thenReturn(itemRequest);

        ItemRequestDto itemRequestDto = requestService.createRequest(user.getId(), requestDtoIn);

        assertThat(itemRequestDto.getId(), equalTo(itemRequest.getId()));
        assertThat(itemRequestDto.getDescription(), equalTo(itemRequest.getDescription()));
        assertThat(itemRequestDto.getCreated(), equalTo(itemRequest.getCreated()));
    }

    @Test
    void getAll() {
        Mockito
                .when(requestRepository.getAllByRequestorId(anyLong()))
                .thenReturn(List.of(itemRequest));

        List<ItemRequestDto> requestDto = requestService.getAll(user.getId());

        assertThat(requestDto, equalTo(List.of(RequestMapper.mapItemRequestToDto(itemRequest))));
    }

    @Test
    void getById() {
        Mockito
                .when(requestRepository.getById(anyLong()))
                .thenReturn(itemRequest);

        ItemRequestDto itemRequestDto = requestService.getById(user.getId(), itemRequest.getId());

        assertThat(itemRequestDto.getId(), equalTo(itemRequest.getId()));
        assertThat(itemRequestDto.getDescription(), equalTo(itemRequest.getDescription()));
        assertThat(itemRequestDto.getCreated(), equalTo(itemRequest.getCreated()));
    }

    @Test
    void getAllRequestsFromOtherUser() {
        Mockito
                .when(requestRepository.findByRequestor_IdNotOrderByCreatedDesc(anyLong(), any()))
                .thenReturn(List.of(itemRequest));

        List<ItemRequestDto> requestDto = requestService.getAllRequestsFromOtherUser(user.getId(), 0, 10);

        assertThat(requestDto, equalTo(List.of(RequestMapper.mapItemRequestToDto(itemRequest))));
    }
}