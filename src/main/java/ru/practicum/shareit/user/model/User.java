package ru.practicum.shareit.user.model;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

@Data
public class User {
    private long id;
    private String name;
    @Email
    private String email;
    private List<Item> items = new ArrayList<>();
}
