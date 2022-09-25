package ru.practicum.shareit.requests.dto;

import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class RequestMapper {

    public static ItemRequestDto mapItemRequestToDto(ItemRequest request) {
        return new ItemRequestDto(
                request.getId(),
                request.getDescription(),
                request.getCreated(),
                new ArrayList<>()
        );
    }

    public static ItemRequest mapItemRequestDtoInToItemRequest(ItemRequestDtoIn requestDtoIn, User requestor) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(requestDtoIn.getDescription());
        itemRequest.setRequestor(requestor);
        itemRequest.setCreated(LocalDateTime.now());

        return itemRequest;
    }
}
