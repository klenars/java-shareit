package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public User createUser(@RequestBody @Valid User user) {
        return userService.create(user);
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable long userId) {
        return userService.get(userId);
    }

    @PatchMapping("/{userId}")
    public User updateUser(@PathVariable long userId,
                           @RequestBody @Valid User user) {
        return userService.update(userId, user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        userService.delete(userId);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAll();
    }
}
