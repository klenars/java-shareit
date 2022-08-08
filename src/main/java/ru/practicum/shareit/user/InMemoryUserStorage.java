package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;

@Repository
public class InMemoryUserStorage implements UserStorage {
    @Override
    public User add(User user) {
        return null;
    }

    @Override
    public User get(long userId) {
        return null;
    }

    @Override
    public User update(long userId, User user) {
        return null;
    }

    @Override
    public void delete(long userId) {

    }
}
