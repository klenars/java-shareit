package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface ItemService {

    ItemDto createItem(long userId, ItemDto itemDto);

    ItemDtoWithBooking getItemById(long itemId, long userId);

    ItemDto updateItem(long userId, long itemId, ItemDto itemDto);

    CommentDtoOut addComment(long userId, long itemId, Comment comment);

    List<ItemDtoWithBooking> getAllItemsByOwner(long userId, int from, int size);

    List<ItemDto> getItemBySubstring(long userId, String text, int from, int size);
}
