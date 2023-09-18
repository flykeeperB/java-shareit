package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findRequestByRequestorIdOrderByCreatedDesc(Long requestorId);

    @Query("select re from ItemRequest re where re.requestor.id <> ?1")
    Page<ItemRequest> findAll(Long userId, Pageable pageable);

}
