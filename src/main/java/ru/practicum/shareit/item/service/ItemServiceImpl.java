package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.FieldValidationException;
import ru.practicum.shareit.exception.UserDoesntExistException;
import ru.practicum.shareit.exception.UserValidationException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public ItemDto createItem(long userId, ItemDto itemDto) {
        checkUserExist(userId);
        checkItemDtoFields(itemDto);

        Item item = ItemMapper.mapDtoToItem(itemDto);

        item.setOwner(userStorage.get(userId));

        return ItemMapper.mapItemToDto(itemStorage.createItem(item));
    }

    @Override
    public ItemDto getItemById(long itemId) {
        return ItemMapper.mapItemToDto(itemStorage.getItemById(itemId));
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        Item itemForUpdate = itemStorage.getItemById(itemId);
        checkUserExist(userId);
        checkItemsOwner(userId, itemForUpdate);

        if (itemDto.getName() != null) {
            itemForUpdate.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            itemForUpdate.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            itemForUpdate.setAvailable(itemDto.getAvailable());
        }

        return ItemMapper.mapItemToDto(itemStorage.updateItem(itemId, itemForUpdate));
    }

    @Override
    public List<ItemDto> getAllItemsByOwner(long userId) {
        checkUserExist(userId);
        return itemStorage.getAllItemsByOwner(userId).stream()
                .map(ItemMapper::mapItemToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getItemBySubstring(long userId, String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemStorage.getItemBySubstring(userId, text).stream()
                .map(ItemMapper::mapItemToDto)
                .collect(Collectors.toList());
    }

    private void checkItemsOwner(long userId, Item item) {
        if (item.getOwner().getId() != userId) {
            throw new UserValidationException(
                    String.format("User id=%d isn't owner of Item id=%d!", userId, item.getId())
            );
        }
    }

    private void checkUserExist(long userId) {
        if (userStorage.get(userId) == null) {
            throw new UserDoesntExistException(String.format("User id=%d doesn't exist!", userId));
        }
    }

    private void checkItemDtoFields(ItemDto itemDto) {
        String errorMessage = null;
        if (itemDto.getName() == null || itemDto.getName().isBlank()) {
            errorMessage = "Item can't be without name!";
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            errorMessage = "Item can't be without description!";
        }
        if (itemDto.getAvailable() == null) {
            errorMessage = "Item can't be without available status!";
        }

        if (errorMessage != null) {
            throw new FieldValidationException(errorMessage);
        }
    }
}
