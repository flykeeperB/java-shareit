package ru.practicum.shareit;

import java.util.List;
import java.util.Optional;

public interface Controller<T extends AbstractDto> {

    T create(T source, Optional<Long> userId);

    T retrieve(Long id, Optional<Long> userId);

    List<T> retrieve(Optional<Long> userId);

    List<T> retrieve(List<Long> ids, Optional<Long> userId);

    T update(T source, Long targetId, Optional<Long> userId);

    void delete(Long id, Optional<Long> userId);

}
