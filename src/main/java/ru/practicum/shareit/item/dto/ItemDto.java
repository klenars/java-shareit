package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemDto {
    private long itemId;
    private String name;
    private String description;
    private boolean available;
}