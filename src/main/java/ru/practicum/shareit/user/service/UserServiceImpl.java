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

    @Override
    public UserDto create(UserDto userDto) {
        checkEmail(userDto.getEmail());
        User user = UserMapper.mapDtoToUser(userDto);
        return UserMapper.mapUserToDto(userStorage.add(user));
    }

    @Override
    public UserDto get(long userId) {
        return UserMapper.mapUserToDto(userStorage.get(userId));
    }

    @Override
    public UserDto update(long userId, UserDto userDto) {
        User userForUpdate = userStorage.get(userId);
        if (userDto.getName() != null) {
            userForUpdate.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            checkEmail(userDto.getEmail());
            userForUpdate.setEmail(userDto.getEmail());
        }
        return UserMapper.mapUserToDto(userStorage.update(userId, userForUpdate));
    }

    @Override
    public void delete(long userId) {
        userStorage.delete(userId);
    }

    @Override
    public List<UserDto> getAll() {
        return userStorage.getAll().stream()
                .map(UserMapper::mapUserToDto)
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
