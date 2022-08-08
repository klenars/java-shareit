package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.FieldUniqueException;
import ru.practicum.shareit.exception.FieldValidationException;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    private final UserMapper userMapper;

    @Override
    public UserDto create(User user) {
        checkEmail(user.getEmail());
        return userMapper.mapUserToDto(userStorage.add(user));
    }

    @Override
    public UserDto get(long userId) {
        return userMapper.mapUserToDto(userStorage.get(userId));
    }

    @Override
    public UserDto update(long userId, User user) {
        User userForUpdate = userStorage.get(userId);
        if (user.getName() != null) {
            userForUpdate.setName(user.getName());
        }
        if (user.getEmail() != null) {
            checkEmail(user.getEmail());
            userForUpdate.setEmail(user.getEmail());
        }
        return userMapper.mapUserToDto(userStorage.update(userId, userForUpdate));
    }

    @Override
    public void delete(long userId) {
        userStorage.delete(userId);
    }

    @Override
    public List<UserDto> getAll() {
        return userStorage.getAll().stream()
                .map(userMapper::mapUserToDto)
                .collect(Collectors.toList());
    }

    private void checkEmail(final String email) {
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new FieldValidationException("Email is wrong!");
        }
        if (getAll().stream().anyMatch(user -> user.getEmail().equals(email))) {
            throw new FieldUniqueException(String.format("User with email %s already exists!", email));
        }
    }
}
