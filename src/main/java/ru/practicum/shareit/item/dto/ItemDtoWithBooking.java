package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;

@Data
public class ItemDtoWithBooking extends ItemDto{
    BookingDtoForItem lastBooking;
    BookingDtoForItem nextBooking;
}
