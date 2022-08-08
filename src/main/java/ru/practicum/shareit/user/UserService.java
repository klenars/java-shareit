package ru.practicum.shareit.user;

public interface UserService {

    User create(User user);

    User get(long userId);

    User update(long userId, User user);

    void delete(long userId);
}
