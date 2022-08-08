package ru.practicum.shareit.user;

public interface UserStorage {

    User add(User user);

    User get(long userId);

    User update(long userId, User user);

    void delete(long userId);
}
