package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.EmailUniqueException;
import ru.practicum.shareit.exception.FieldValidationException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Override
    public User create(User user) {
        checkEmail(user.getEmail());
        return userStorage.add(user);
    }

    @Override
    public User get(long userId) {
        return userStorage.get(userId);
    }

    @Override
    public User update(long userId, User user) {
        User userForUpdate = get(userId);
        if (user.getName() != null) {
            userForUpdate.setName(user.getName());
        }
        if (user.getEmail() != null) {
            checkEmail(user.getEmail());
            userForUpdate.setEmail(user.getEmail());
        }
        return userStorage.update(userId, userForUpdate);
    }

    @Override
    public void delete(long userId) {
        userStorage.delete(userId);
    }

    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }

    private void checkEmail(final String email) {
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new FieldValidationException("Email is wrong!");
        }
        if (getAll().stream().anyMatch(user -> user.getEmail().equals(email))) {
            throw new EmailUniqueException(String.format("User with email %s already exists!", email));
        }
    }
}
