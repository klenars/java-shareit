package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserIntegrationTest {

    private final UserService userService;

    private final UserDto userDto = new UserDto(
            0,
            "User",
            "user@mail.ru"
    );

    @Test
    public void createUserTest() {
        UserDto userRequest = userService.create(userDto);

        assertThat(userRequest.getId(), equalTo(1L));
        assertThat(userRequest.getName(), equalTo(userDto.getName()));
        assertThat(userRequest.getEmail(), equalTo(userDto.getEmail()));
    }
}
