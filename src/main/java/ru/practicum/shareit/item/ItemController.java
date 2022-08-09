package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestBody Item item
    ) {
        return itemService.createItem(userId, item);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(
            @PathVariable long itemId
    ) {
        return itemService.getItemById(itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long itemId,
            @RequestBody Item item
    ) {
        return itemService.updateItem(userId, itemId, item);
    }

    @GetMapping
    public List<ItemDto> getAllItemsByOwner(
            @RequestHeader("X-Sharer-User-Id") long userId
    ) {
        return itemService.getAllItemsByOwner(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemBySubstring(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam String text
    ) {
        return itemService.getItemBySubstring(userId, text);
    }
}
