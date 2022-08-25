package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;
import ru.practicum.shareit.item.model.Comment;

import java.util.ArrayList;
import java.util.List;

@Data
public class ItemDtoWithBooking extends ItemDto{
    BookingDtoForItem lastBooking;
    BookingDtoForItem nextBooking;
    List<Comment> comments = new ArrayList<>();
}
