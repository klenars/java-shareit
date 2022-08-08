package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    UserDto create(User user);

    UserDto get(long userId);

    UserDto update(long userId, User user);

    void delete(long userId);

    List<UserDto> getAll();
}
