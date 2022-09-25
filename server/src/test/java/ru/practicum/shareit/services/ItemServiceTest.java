package ru.practicum.shareit.services;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDtoOut;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoWithBooking;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceTest {

    private final ItemService itemService;

    @MockBean
    private final UserRepository userRepository;

    @MockBean
    private final ItemRepository itemRepository;

    @MockBean
    private final BookingRepository bookingRepository;

    @MockBean
    private final CommentRepository commentRepository;

    private final User user = new User(
            888L,
            "Ivan Ivanov",
            "Ivan@yandex.ru"
    );

    private final Item item = new Item(
            888L,
            "Otvertka",
            "Good Otvertka",
            true,
            user,
            null
    );

    private final Comment comment = new Comment(
            888L,
            "Comment",
            item,
            user,
            LocalDateTime.now()
    );

    @Test
    void createItem() {
        Mockito
                .when(itemRepository.save(any()))
                .thenReturn(item);

        ItemDto itemDto = itemService.createItem(user.getId(), ItemMapper.mapItemToDto(item));

        assertThat(itemDto.getId(), equalTo(item.getId()));
        assertThat(itemDto.getName(), equalTo(item.getName()));
        assertThat(itemDto.getDescription(), equalTo(item.getDescription()));
        assertThat(itemDto.getAvailable(), equalTo(item.getAvailable()));
    }

    @Test
    void getItemById() {
        Mockito
                .when(itemRepository.getReferenceById(anyLong()))
                .thenReturn(item);

        ItemDtoWithBooking itemDto = itemService.getItemById(item.getId(), user.getId());

        assertThat(itemDto.getId(), equalTo(item.getId()));
        assertThat(itemDto.getName(), equalTo(item.getName()));
        assertThat(itemDto.getDescription(), equalTo(item.getDescription()));
        assertThat(itemDto.getAvailable(), equalTo(item.getAvailable()));
    }

    @Test
    void updateItem() {
        Mockito
                .when(itemRepository.save(any()))
                .thenReturn(item);

        ItemDto itemDto = itemService.createItem(user.getId(), ItemMapper.mapItemToDto(item));

        assertThat(itemDto.getId(), equalTo(item.getId()));
        assertThat(itemDto.getName(), equalTo(item.getName()));
        assertThat(itemDto.getDescription(), equalTo(item.getDescription()));
        assertThat(itemDto.getAvailable(), equalTo(item.getAvailable()));
    }

    @Test
    void addComment() {
        Mockito
                .when(bookingRepository.existsByItem_IdAndBooker_IdAndEndBefore(anyLong(), anyLong(), any()))
                .thenReturn(true);
        Mockito
                .when(userRepository.getReferenceById(anyLong()))
                .thenReturn(user);
        Mockito
                .when(commentRepository.save(any()))
                .thenReturn(comment);

        CommentDtoOut commentDtoOut = itemService.addComment(user.getId(), item.getId(), comment);

        assertThat(commentDtoOut.getId(), equalTo(comment.getId()));
        assertThat(commentDtoOut.getText(), equalTo(comment.getText()));
        assertThat(commentDtoOut.getAuthorName(), equalTo(comment.getAuthor().getName()));
        assertThat(commentDtoOut.getCreated(), equalTo(comment.getCreated()));
    }

    @Test
    void getAllItemsByOwner() {
        Mockito
                .when(itemRepository.findByOwnerId(anyLong(), any()))
                .thenReturn(List.of(item));

        List<ItemDtoWithBooking> items = itemService.getAllItemsByOwner(user.getId(), 0, 10);

        assertThat(items, equalTo(List.of(ItemMapper.mapItemToDtoWithBooking(item))));
    }

    @Test
    void getItemBySubstring() {
        Mockito
                .when(itemRepository.findBySubstring(anyString(), any()))
                .thenReturn(List.of(item));
        List<ItemDto> items = itemService.getItemBySubstring(user.getId(), "text", 0, 10);

        assertThat(items, equalTo(List.of(ItemMapper.mapItemToDto(item))));
    }
}