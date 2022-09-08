package ru.practicum.shareit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @Autowired
    private MockMvc mockMvc;

    private final UserDto userDto = new UserDto(
            888L,
            "Ivan Ivanov",
            "Ivan@yandex.ru"
    );

    @Test
    void createUser() throws Exception {
        Mockito
                .when(userService.create(Mockito.any()))
                .thenReturn(userDto);

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName()), String.class))
                .andExpect(jsonPath("$.email", is(userDto.getEmail()), String.class));
    }


    @Test
    void getUser() throws Exception {
        Mockito
                .when(userService.get(Mockito.anyLong()))
                .thenReturn(userDto);

        mockMvc.perform(get("/users/888"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName()), String.class))
                .andExpect(jsonPath("$.email", is(userDto.getEmail()), String.class));
    }

    @Test
    void updateUser() throws Exception {
        Mockito
                .when(userService.update(Mockito.anyLong(), Mockito.any()))
                .thenReturn(userDto);

        mockMvc.perform(patch("/users/888")
                        .content(objectMapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto.getName()), String.class))
                .andExpect(jsonPath("$.email", is(userDto.getEmail()), String.class));
    }

    @Test
    void deleteUser() throws Exception {
        mockMvc.perform(delete("/users/888"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllUsers() throws Exception {
        Mockito
                .when(userService.getAll())
                .thenReturn(List.of(userDto));

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(userDto))));
    }

    static class ItemControllerTest {

        @Test
        void createItem() {
        }

        @Test
        void getItemById() {
        }

        @Test
        void updateItem() {
        }

        @Test
        void getAllItemsByOwner() {
        }

        @Test
        void getItemBySubstring() {
        }

        @Test
        void addComment() {
        }
    }
}