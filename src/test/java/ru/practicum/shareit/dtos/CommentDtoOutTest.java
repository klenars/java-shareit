package ru.practicum.shareit.dtos;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.CommentDtoOut;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
class CommentDtoOutTest {

    @Autowired
    private JacksonTester<CommentDtoOut> jacksonTester;

    private final CommentDtoOut commentDtoOut = new CommentDtoOut(
            888,
            "text",
            "Ivan",
            LocalDateTime.of(2022, 2, 22, 22, 22, 22)
    );

    @Test
    void CommentDtoTest() throws IOException {
        JsonContent<CommentDtoOut> res = jacksonTester.write(commentDtoOut);

        assertThat(res).extractingJsonPathNumberValue("$.id").isEqualTo(888);
        assertThat(res).extractingJsonPathStringValue("$.text").isEqualTo(commentDtoOut.getText());
        assertThat(res).extractingJsonPathStringValue("$.authorName").isEqualTo(commentDtoOut.getAuthorName());
    }
}