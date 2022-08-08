package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {

    User add(User user);

    User get(long userId);

    User update(long userId, User user);

    void delete(long userId);

    List<User> getAll();
}
