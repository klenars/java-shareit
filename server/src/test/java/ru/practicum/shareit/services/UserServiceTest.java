package ru.practicum.shareit.services;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceTest {

    private final UserService userService;

    @MockBean
    private final UserRepository userRepository;

    private final User user = new User(
            888L,
            "Ivan Ivanov",
            "Ivan@yandex.ru"
    );

    @Test
    void create() {
        Mockito
                .when(userRepository.save(any()))
                .thenReturn(user);

        UserDto userDto = userService.create(UserMapper.mapUserToDto(user));

        assertThat(userDto.getId(), equalTo(user.getId()));
        assertThat(userDto.getName(), equalTo(user.getName()));
        assertThat(userDto.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    void get() {
        Mockito
                .when(userRepository.save(any()))
                .thenReturn(user);
        Mockito
                .when(userRepository.getReferenceById(any()))
                .thenReturn(user);

        UserDto userDto = userService.get(user.getId());

        assertThat(userDto.getId(), equalTo(user.getId()));
        assertThat(userDto.getName(), equalTo(user.getName()));
        assertThat(userDto.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    void update() {
        Mockito
                .when(userRepository.save(any()))
                .thenReturn(user);
        Mockito
                .when(userRepository.getReferenceById(any()))
                .thenReturn(user);

        UserDto userDto = userService.update(user.getId(), UserMapper.mapUserToDto(user));

        assertThat(userDto.getId(), equalTo(user.getId()));
        assertThat(userDto.getName(), equalTo(user.getName()));
        assertThat(userDto.getEmail(), equalTo(user.getEmail()));
    }

    @Test
    void getAll() {
        Mockito
                .when(userRepository.findAll())
                .thenReturn(List.of(user));

        List<UserDto> users = userService.getAll();

        assertThat(users, equalTo(List.of(UserMapper.mapUserToDto(user))));
    }
}