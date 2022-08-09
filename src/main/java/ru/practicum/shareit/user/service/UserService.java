package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.User;

import java.util.List;

public interface UserService {

    User create(User user);

    User get(long userId);

    User update(long userId, User user);

    void delete(long userId);

    List<User> getAll();
}
