package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;

import java.util.List;

public interface ItemService {

    List<ItemDto> getItemBySubstring(long userId, String text);

    ItemDto createItem(long userId, ItemDto itemDto);

    ItemDtoWithBooking getItemById(long itemId, long userId);

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);

    List<ItemDtoWithBooking> getAllItemsByOwner(long userId);
}
