package ru.practicum.shareit.requests.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.FieldValidationException;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestDtoIn;
import ru.practicum.shareit.requests.dto.RequestMapper;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;

    @Override
    public ItemRequestDto createRequest(long userId, ItemRequestDtoIn request) {
        checkRequestDescription(request.getDescription());

        User requestor = userRepository.getById(userId);
        ItemRequest itemRequest = RequestMapper.mapItemRequestDtoInToItemRequest(request, requestor);

        return RequestMapper.mapItemRequestToDto(requestRepository.save(itemRequest));
    }

    private void checkRequestDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new FieldValidationException("Request can't be without description!");
        }
    }
}
