package ru.practicum.shareit.dtos;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.requests.dto.ItemRequestDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoTest {

    @Autowired
    private JacksonTester<ItemRequestDto> jacksonTester;

    private final ItemRequestDto itemRequestDto = new ItemRequestDto(
            888,
            "description",
            LocalDateTime.of(2022, 2, 22, 22, 22, 22),
            Collections.emptyList()
    );

    @Test
    void requestDtoTest() throws IOException {
        JsonContent<ItemRequestDto> res = jacksonTester.write(itemRequestDto);

        assertThat(res).extractingJsonPathNumberValue("$.id").isEqualTo(888);
        assertThat(res).extractingJsonPathStringValue("$.description").isEqualTo(itemRequestDto.getDescription());
        assertThat(res).extractingJsonPathStringValue("$.created").isEqualTo(itemRequestDto.getCreated().toString());
        assertThat(res).extractingJsonPathArrayValue("$.items").isEqualTo(itemRequestDto.getItems());
    }

}