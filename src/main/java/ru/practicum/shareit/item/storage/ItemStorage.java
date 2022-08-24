package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item createItem(Item item);

    Item getItemById(long itemId);

    Item updateItem(long itemId, Item item);

    List<Item> getAllItemsByOwner(long userId);

    List<Item> getItemBySubstring(long userId, String text);
}
