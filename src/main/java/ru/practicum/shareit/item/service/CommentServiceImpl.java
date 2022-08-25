package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.FieldValidationException;
import ru.practicum.shareit.item.dto.CommentDtoForDb;
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
    public Comment addComment(long userId, long itemId, Comment comment) {
        userRepository.checkUserExist(userId);
        checkCommentAuthor(userId, itemId);

        CommentDtoForDb commentDtoForDb = CommentMapper.mapCommentToDtoForDb(comment);
        commentDtoForDb.setAuthorId(userId);
        commentDtoForDb.setItemId(itemId);
        commentDtoForDb.setCreated(LocalDateTime.now());

        return getCommentFromDtoDb(commentRepository.save(commentDtoForDb));
    }

    private void checkCommentAuthor(long userId, long itemId) {
        if (!bookingRepository.existsByItem_IdAndBooker_IdAndEndBefore(itemId, userId, LocalDateTime.now())) {
            throw new FieldValidationException(
                    String.format("User id=%d hasn't booking item id=%d or booking isn't finished yet!", userId, itemId)
            );
        }
    }

    private Comment getCommentFromDtoDb(CommentDtoForDb commentDtoForDb) {
        Comment comment = CommentMapper.mapDtoDbToComment(commentDtoForDb);
        comment.setItem(itemRepository.getReferenceById(commentDtoForDb.getItemId()));
        comment.setAuthor(userRepository.getReferenceById(commentDtoForDb.getAuthorId()));
        return comment;
    }
}
