package ru.practicum.shareit.dtos;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemDtoTest {

    @Autowired
    private JacksonTester<ItemDto> jacksonTester;

    private final ItemDto itemDto = new ItemDto(
            888,
            "name",
            "descriprion",
            true,
            111
    );

    @Test
    void testItemDto() throws IOException {
        JsonContent<ItemDto> res = jacksonTester.write(itemDto);

        assertThat(res).extractingJsonPathNumberValue("$.id").isEqualTo(888);
        assertThat(res).extractingJsonPathStringValue("$.name").isEqualTo(itemDto.getName());
        assertThat(res).extractingJsonPathStringValue("$.description").isEqualTo(itemDto.getDescription());
        assertThat(res).extractingJsonPathBooleanValue("$.available").isEqualTo(itemDto.getAvailable());
        assertThat(res).extractingJsonPathNumberValue("$.requestId").isEqualTo(111);
    }
}