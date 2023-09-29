package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwnerIdOrderById(Long userId, Pageable pageable);

    @Query("select it " +
            "from Item as it " +
            "join it.owner as u " +
            "where " +
            "(" +
            "UPPER(it.name) like CONCAT('%',UPPER(:searchText),'%') or " +
            "UPPER(it.description) like CONCAT('%',UPPER(:searchText),'%')" +
            ") " +
            "and it.available = true")
    List<Item> findAvailableByNameOrDescriptionContainingSearchText(@Param("searchText") String searchText,
                                                                    Pageable pageable);

    List<Item> findByRequestIdInOrderByRequestId(List<Long> ids);
}
