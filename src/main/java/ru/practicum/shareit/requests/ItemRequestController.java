package ru.practicum.shareit.requests;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestDtoIn;
import ru.practicum.shareit.requests.service.RequestService;

import java.util.List;

/**
 * // TODO .
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
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
            @RequestParam(defaultValue = "0") int from,
            @RequestParam int size
    ) {
        return null;
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getById(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long requestId
    ) {
        return requestService.getById(userId, requestId);
    }
}
