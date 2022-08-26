package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.FieldValidationException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto createItem(long userId, ItemDto itemDto) {
        userRepository.checkUserExist(userId);
        checkItemDtoFields(itemDto);

        Item item = ItemMapper.mapDtoToItem(itemDto);

        item.setOwner(userRepository.getReferenceById(userId));

        return ItemMapper.mapItemToDto(itemRepository.save(item));
    }

    @Override
    public ItemDtoWithBooking getItemById(long itemId, long userId) {
        Item item = itemRepository.getReferenceById(itemId);
        ItemDtoWithBooking itemDtoWithBooking = ItemMapper.mapItemToDtoWithBooking(item);
        if (item.getOwner().getId() == userId) {
            itemDtoWithBooking.setLastBooking(
                    BookingMapper.mapBookingToDtoForItem(
                            bookingRepository.findByItem_IdAndEndBeforeOrderByEndDesc(item.getId(), LocalDateTime.now())
                    )
            );
            itemDtoWithBooking.setNextBooking(
                    BookingMapper.mapBookingToDtoForItem(
                            bookingRepository.findByItem_IdAndStartAfterOrderByStartAsc(item.getId(), LocalDateTime.now())
                    )
            );
        }
        itemDtoWithBooking.setComments(
                commentRepository.findByItem_Id(itemId).stream()
                        .map(CommentMapper::mapCommentToDtoOut)
                        .collect(Collectors.toList())
        );

        return itemDtoWithBooking;
    }

    @Override
    public ItemDto updateItem(long userId, long itemId, ItemDto itemDto) {
        userRepository.checkUserExist(userId);
        Item itemForUpdate = itemRepository.getReferenceById(itemId);
        itemRepository.checkItemsOwner(userId, itemForUpdate);

        if (itemDto.getName() != null) {
            itemForUpdate.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            itemForUpdate.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            itemForUpdate.setAvailable(itemDto.getAvailable());
        }

        return ItemMapper.mapItemToDto(itemRepository.save(itemForUpdate));
    }

    @Override
    public List<ItemDtoWithBooking> getAllItemsByOwner(long userId) {
        userRepository.checkUserExist(userId);
        return itemRepository.findByOwnerId(userId).stream()
                .map(item -> {
                    ItemDtoWithBooking itemDtoWithBooking = ItemMapper.mapItemToDtoWithBooking(item);
                    itemDtoWithBooking.setLastBooking(
                            BookingMapper.mapBookingToDtoForItem(
                                    bookingRepository.findByItem_IdAndEndBeforeOrderByEndDesc(item.getId(), LocalDateTime.now())
                            )
                    );
                    itemDtoWithBooking.setNextBooking(
                            BookingMapper.mapBookingToDtoForItem(
                                    bookingRepository.findByItem_IdAndStartAfterOrderByStartAsc(item.getId(), LocalDateTime.now())
                            )
                    );
                    return itemDtoWithBooking;
                })
                .sorted(Comparator.comparingLong(ItemDtoWithBooking::getId))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getItemBySubstring(long userId, String text) {
        userRepository.checkUserExist(userId);
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.findBySubstring(text).stream()
                .map(ItemMapper::mapItemToDto)
                .collect(Collectors.toList());
    }

    private void checkItemDtoFields(ItemDto itemDto) {
        String errorMessage = null;
        if (itemDto.getName() == null || itemDto.getName().isBlank()) {
            errorMessage = "Item can't be without name!";
        }
        if (itemDto.getDescription() == null || itemDto.getDescription().isBlank()) {
            errorMessage = "Item can't be without description!";
        }
        if (itemDto.getAvailable() == null) {
            errorMessage = "Item can't be without available status!";
        }

        if (errorMessage != null) {
            throw new FieldValidationException(errorMessage);
        }
    }
}
