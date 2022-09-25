package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class ItemDto {
    private long id;
    private String name;
    @NotBlank
    private String description;
    private Boolean available;
    private long requestId;
}