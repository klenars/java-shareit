package ru.practicum.shareit.dtos;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoTest {

    @Autowired
    private JacksonTester<BookingRequestDto> jacksonTesterReq;

    @Autowired
    private JacksonTester<BookingResponseDto> jacksonTesterResp;

    private final BookingRequestDto bookingRequestDto = new BookingRequestDto(
            888,
            LocalDateTime.of(2022, 2, 22, 22, 22, 22),
            LocalDateTime.of(2022, 2, 22, 22, 22, 23)
    );

    private final BookingResponseDto bookingResponseDto = new BookingResponseDto(
            999,
            LocalDateTime.of(2022, 2, 22, 22, 22, 22),
            LocalDateTime.of(2022, 2, 22, 22, 22, 23),
            BookingStatus.APPROVED,
            null,
            null
    );

    @Test
    void bookingRequestDtoTest() throws IOException {
        JsonContent<BookingRequestDto> result = jacksonTesterReq.write(bookingRequestDto);

        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(888);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(bookingRequestDto.getStart().toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(bookingRequestDto.getEnd().toString());
    }

    @Test
    void bookingResponseDtoTest() throws IOException {
        JsonContent<BookingResponseDto> result = jacksonTesterResp.write(bookingResponseDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(999);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(bookingResponseDto.getStart().toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(bookingResponseDto.getEnd().toString());
    }

}