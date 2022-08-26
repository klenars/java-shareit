package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.model.Comment;

public interface CommentService {
    CommentDtoOut addComment(long userId, long itemId, Comment comment);
}
