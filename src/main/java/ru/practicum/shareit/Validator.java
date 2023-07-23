package ru.practicum.shareit;

import java.util.List;
import java.util.Optional;

public interface Validator<T> {
    void forCreate(T source, Optional<Long> userId);

    void forRetrieve(Long id, Optional<Long> userId);

    void forRetrieve(Optional<Long> userId);

    void forRetrieve(List<Long> ids, Optional<Long> userId);

    void forUpdate(T source, Optional<Long> userId);

    void forDelete(Long id, Optional<Long> userId);

    String getName();
}
