package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.FieldValidationException;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

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

    private void checkCommentAuthor(long userId, long itemId) {
        if (!bookingRepository.existsByItem_IdAndBooker_IdAndEndBefore(itemId, userId, LocalDateTime.now())) {
            throw new FieldValidationException(
                    String.format("User id=%d hasn't booking item id=%d or booking isn't finished yet!", userId, itemId)
            );
        }
    }
}
