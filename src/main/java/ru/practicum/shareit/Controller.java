package ru.practicum.shareit;

import java.util.Optional;

public interface Controller<T extends AbstractDto> extends Crud<T> {

    T update(T source, Long targetId, Optional<Long> userId);

    String getName();
}
