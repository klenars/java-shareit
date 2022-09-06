package ru.practicum.shareit.requests.service;

import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestDtoIn;

public interface RequestService {
    ItemRequestDto createRequest(long userId, ItemRequestDtoIn request);
}
