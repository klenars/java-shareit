package ru.practicum.shareit.dtos;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserDtoTest {

    @Autowired
    private JacksonTester<UserDto> jacksonTester;

    private final UserDto userDto = new UserDto(
            888,
            "Ivan",
            "Ivan@ya.ru"
    );

    @Test
    void testUserDto() throws IOException {
        JsonContent<UserDto> res = jacksonTester.write(userDto);

        assertThat(res).extractingJsonPathNumberValue("$.id").isEqualTo(888);
        assertThat(res).extractingJsonPathStringValue("$.name").isEqualTo(userDto.getName());
        assertThat(res).extractingJsonPathStringValue("$.email").isEqualTo(userDto.getEmail());
    }

}