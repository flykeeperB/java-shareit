package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.ShareItRepository;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends ShareItRepository<Comment> {

    List<Comment> findByItemId(Long itemId);

}
