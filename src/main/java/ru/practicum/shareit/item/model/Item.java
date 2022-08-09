package ru.practicum.shareit.item.model;

import lombok.Data;

@Data
public class Item {
    private long itemId;
    private String name;
    private String description;
    private boolean available;
    private long ownerId;
}
