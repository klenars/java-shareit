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
    private final ItemMapper itemMapper;

    @Override
    public ItemDto createItem(long userId, Item item) {
        checkUserExist(userId);
        checkItemFields(item);

        item.setOwnerId(userId);

        return itemMapper.mapItemToDto(itemStorage.createItem(item));
    }

    @Override
    public ItemDto getItemById(long itemId) {
        return itemMapper.mapItemToDto(itemStorage.getItemById(itemId));
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, Item item) {
        Item itemForUpdate = itemStorage.getItemById(itemId);
        checkUserExist(userId);
        checkItemsOwner(userId, itemForUpdate);

        if (item.getName() != null) {
            itemForUpdate.setName(item.getName());
        }
        if (item.getDescription() != null) {
            itemForUpdate.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemForUpdate.setAvailable(item.getAvailable());
        }

        return itemMapper.mapItemToDto(itemStorage.updateItem(itemId, itemForUpdate));
    }

    @Override
    public List<ItemDto> getAllItemsByOwner(long userId) {
        checkUserExist(userId);
        return itemStorage.getAllItemsByOwner(userId).stream()
                .map(itemMapper::mapItemToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getItemBySubstring(long userId, String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemStorage.getItemBySubstring(userId, text).stream()
                .map(itemMapper::mapItemToDto)
                .collect(Collectors.toList());
    }

    private void checkItemsOwner(long userId, Item item) {
        if (item.getOwnerId() != userId) {
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

    private void checkItemFields(Item item) {
        String errorMessage = null;
        if (item.getName() == null || item.getName().isBlank()) {
            errorMessage = "Item can't be without name!";
        }
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            errorMessage = "Item can't be without description!";
        }
        if (item.getAvailable() == null) {
            errorMessage = "Item can't be without available status!";
        }

        if (errorMessage != null) {
            throw new FieldValidationException(errorMessage);
        }
    }
}
