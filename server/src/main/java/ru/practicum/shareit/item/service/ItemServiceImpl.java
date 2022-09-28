package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.FieldValidationException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.requests.model.ItemRequest;
import ru.practicum.shareit.requests.repository.RequestRepository;
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
    private final RequestRepository requestRepository;

    @Override
    public ItemDto createItem(long userId, ItemDto itemDto) {
        userRepository.checkUserExist(userId);
        checkItemDtoFields(itemDto);

        ItemRequest request = itemDto.getRequestId() == 0 ? null : requestRepository.getById(itemDto.getRequestId());

        Item item = ItemMapper.mapDtoToItem(itemDto, request);

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
                            bookingRepository.findByItem_IdAndStartAfterOrderByStartAsc(item.getId(), LocalDateTime.now()).stream().findAny().orElse(null)
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
    public List<ItemDtoWithBooking> getAllItemsByOwner(long userId, int from, int size) {
        userRepository.checkUserExist(userId);
        return itemRepository.findByOwnerId(userId, PageRequest.of(getPageNumber(from, size), size)).stream()
                .map(item -> {
                    ItemDtoWithBooking itemDtoWithBooking = ItemMapper.mapItemToDtoWithBooking(item);
                    itemDtoWithBooking.setLastBooking(
                            BookingMapper.mapBookingToDtoForItem(
                                    bookingRepository.findByItem_IdAndEndBeforeOrderByEndDesc(item.getId(), LocalDateTime.now())
                            )
                    );
                    itemDtoWithBooking.setNextBooking(
                            BookingMapper.mapBookingToDtoForItem(
                                    bookingRepository.findByItem_IdAndStartAfterOrderByStartAsc(item.getId(), LocalDateTime.now()).stream().findAny().orElse(null)
                            )
                    );
                    return itemDtoWithBooking;
                })
                .sorted(Comparator.comparingLong(ItemDtoWithBooking::getId))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getItemBySubstring(long userId, String text, int from, int size) {
        userRepository.checkUserExist(userId);
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.findBySubstring(text, PageRequest.of(getPageNumber(from, size), size)).stream()
                .map(ItemMapper::mapItemToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDtoOut addComment(long userId, long itemId, Comment comment) {
        userRepository.checkUserExist(userId);
        checkCommentAuthor(userId, itemId);
        if (comment.getText().isBlank()) {
            throw new FieldValidationException("Comment can't be without text!");
        }

        comment.setItem(itemRepository.getReferenceById(itemId));
        comment.setAuthor(userRepository.getReferenceById(userId));
        comment.setCreated(LocalDateTime.now());

        return CommentMapper.mapCommentToDtoOut(commentRepository.save(comment));
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

    private void checkCommentAuthor(long userId, long itemId) {
        if (!bookingRepository.existsByItem_IdAndBooker_IdAndEndBefore(itemId, userId, LocalDateTime.now())) {
            throw new FieldValidationException(
                    String.format("User id=%d hasn't booking item id=%d or booking isn't finished yet!", userId, itemId)
            );
        }
    }

    private int getPageNumber(int from, int size) {
        if (from < 0) {
            throw new FieldValidationException("parameter from must not be less than zero");
        }
        return from / size;
    }
}
