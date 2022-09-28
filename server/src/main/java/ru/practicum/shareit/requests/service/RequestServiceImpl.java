package ru.practicum.shareit.requests.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.FieldValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.requests.dto.ItemRequestDto;
import ru.practicum.shareit.requests.dto.ItemRequestDtoIn;
import ru.practicum.shareit.requests.dto.RequestMapper;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.RequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDto createRequest(long userId, ItemRequestDtoIn request) {

        User requestor = userRepository.getById(userId);
        ItemRequest itemRequest = RequestMapper.mapItemRequestDtoInToItemRequest(request, requestor);

        return RequestMapper.mapItemRequestToDto(requestRepository.save(itemRequest));
    }

    @Override
    public ItemRequestDto getById(long userId, long requestId) {
        userRepository.checkUserExist(userId);
        ItemRequestDto itemRequestDto = RequestMapper.mapItemRequestToDto(requestRepository.getById(requestId));
        addItemsToRequestDto(itemRequestDto);

        return itemRequestDto;
    }

    @Override
    public List<ItemRequestDto> getAll(long userId) {
        userRepository.checkUserExist(userId);

        return requestRepository.getAllByRequestorId(userId).stream()
                .map(RequestMapper::mapItemRequestToDto)
                .peek(this::addItemsToRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> getAllRequestsFromOtherUser(long userId, int from, int size) {
        userRepository.checkUserExist(userId);

        return requestRepository.findByRequestor_IdNotOrderByCreatedDesc(
                        userId, PageRequest.of(getPageNumber(from, size), size)
                ).stream()
                .map(RequestMapper::mapItemRequestToDto)
                .peek(this::addItemsToRequestDto)
                .collect(Collectors.toList());
    }

    private void addItemsToRequestDto(ItemRequestDto itemRequestDto) {
        List<ItemDto> items = itemRepository.findByRequestId(itemRequestDto.getId()).stream()
                .map(ItemMapper::mapItemToDto)
                .collect(Collectors.toList());
        itemRequestDto.setItems(items);
    }

    private int getPageNumber(int from, int size) {
        if (from < 0) {
            throw new FieldValidationException("parameter from must not be less than zero");
        }
        return from / size;
    }
}
