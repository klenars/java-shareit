package ru.practicum.shareit.item.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryItemStorage implements ItemStorage {

    private final Map<Long, Item> items = new HashMap<>();
    private static long id = 0;

    @Override
    public Item createItem(Item item) {
        long idForNewItem = getId();
        item.setId(idForNewItem);
        items.put(idForNewItem, item);
        return item;
    }

    @Override
    public Item getItemById(long itemId) {
        return items.get(itemId);
    }

    @Override
    public Item updateItem(long itemId, Item item) {
        items.put(itemId, item);
        return item;
    }

    @Override
    public List<Item> getAllItemsByOwner(long userId) {
        return items.values().stream()
                .filter(item -> item.getOwnerId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> getItemBySubstring(long userId, String text) {
        Set<Item> foundItems =
                items.values().stream()
                        .filter(Item::getAvailable)
                        .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase()))
                        .collect(Collectors.toSet());

        foundItems.addAll(
                items.values().stream()
                        .filter(Item::getAvailable)
                        .filter(item -> item.getDescription().toLowerCase().contains(text.toLowerCase()))
                        .collect(Collectors.toSet())
        );

        return new ArrayList<>(foundItems);
    }

    private long getId() {
        return ++id;
    }
}
