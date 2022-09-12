package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestDtoIn;
import ru.practicum.shareit.requests.service.RequestService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final RequestService requestService;

    @PostMapping
    public ItemRequestDto createRequest(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestBody ItemRequestDtoIn request
    ) {
        return requestService.createRequest(userId, request);
    }

    @GetMapping
    public List<ItemRequestDto> getAllRequests(
            @RequestHeader("X-Sharer-User-Id") long userId
    ) {
        return requestService.getAll(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequestsFromOtherUser(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PositiveOrZero @RequestParam(defaultValue = "0") int from,
            @Positive @RequestParam(defaultValue = "10") int size
    ) {
        return requestService.getAllRequestsFromOtherUser(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getById(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long requestId
    ) {
        return requestService.getById(userId, requestId);
    }
}
