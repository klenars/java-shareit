package ru.practicum.shareit.requests.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemRequestDtoIn {
    private String description;
    private long requestorId;
}
