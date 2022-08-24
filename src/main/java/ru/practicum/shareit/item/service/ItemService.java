package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    List<ItemDto> getItemBySubstring(long userId, String text);

    ItemDto createItem(long userId, ItemDto itemDto);

    ItemDto getItemById(long itemId);

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);

    List<ItemDto> getAllItemsByOwner(long userId);
}
