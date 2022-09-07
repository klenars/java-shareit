package ru.practicum.shareit.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.requests.model.ItemRequest;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface RequestRepository extends JpaRepository<ItemRequest, Long> {

    default ItemRequest getById(long id) {
        return findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Request id=%d not found!", id)));
    }

    @Query("select i from ItemRequest i where i.requestor.id = ?1 order by i.created DESC")
    List<ItemRequest> getAllByRequestorId(long id);
}
