package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Override
    public User create(User user) {
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
