package ru.practicum.shareit;

import java.util.List;

public interface Mapper<T extends AbstractModel, E extends AbstractDto> {

    E toDto(T source);

    T fromDto(E source);

    List<E> toDto(List<T> source);

}
