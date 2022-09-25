package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto create(UserDto userDto) {
        User user = UserMapper.mapDtoToUser(userDto);
        return UserMapper.mapUserToDto(userRepository.save(user));
    }

    @Override
    public UserDto get(long userId) {
        return UserMapper.mapUserToDto(userRepository.getReferenceById(userId));
    }

    @Override
    public UserDto update(long userId, UserDto userDto) {
        User userForUpdate = userRepository.getReferenceById(userId);
        if (userDto.getName() != null) {
            userForUpdate.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            userForUpdate.setEmail(userDto.getEmail());
        }
        return UserMapper.mapUserToDto(userRepository.save(userForUpdate));
    }

    @Override
    public void delete(long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::mapUserToDto)
                .collect(Collectors.toList());
    }
}
