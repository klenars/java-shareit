package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.dto.CommentDtoForDb;

public interface CommentRepository extends JpaRepository<CommentDtoForDb, Long> {


}
