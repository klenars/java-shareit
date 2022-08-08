package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private static long id = 0;

    @Override
    public User add(User user) {
        long id = getId();
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @Override
    public User get(long userId) {
        return users.get(userId);
    }

    @Override
    public User update(long userId, User user) {
        users.put(userId, user);
        return user;
    }

    @Override
    public void delete(long userId) {
        users.remove(userId);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    private long getId() {
        return ++id;
    }
}
