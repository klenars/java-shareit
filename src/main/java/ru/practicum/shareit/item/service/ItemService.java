package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    List<ItemDto> getItemBySubstring(long userId, String text);

    ItemDto createItem(long userId, Item item);

    ItemDto getItemById(long itemId);

    ItemDto updateItem(long userId, long itemId, Item item);

    List<ItemDto> getAllItemsByOwner(long userId);
}
