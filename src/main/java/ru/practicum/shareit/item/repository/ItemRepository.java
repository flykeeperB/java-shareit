package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.ShareItRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends ShareItRepository<Item> {

    List<Item> findByOwnerId(Long userId);

    @Query("select it " +
            "from Item as it " +
            "join it.owner as u " +
            "where " +
            "(" +
            "UPPER(it.name) like CONCAT('%',UPPER(:searchText),'%') or " +
            "UPPER(it.description) like CONCAT('%',UPPER(:searchText),'%')" +
            ") " +
            "and it.available = true")
    List<Item> findAvailableByNameOrDescriptionContainingSearchText(@Param("searchText") String searchText);
}
