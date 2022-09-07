package ru.practicum.shareit.requests.service;

import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestDtoIn;

import java.util.List;

public interface RequestService {
    ItemRequestDto createRequest(long userId, ItemRequestDtoIn request);

    List<ItemRequestDto> getAll(long userId);

    ItemRequestDto getById(long userId, long requestId);
}
