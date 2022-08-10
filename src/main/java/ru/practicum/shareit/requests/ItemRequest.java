package ru.practicum.shareit.requests;

import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class ItemRequest {
    private long id;
    private String description;
    private User requestor;
    private LocalDateTime created;
}
