package ru.practicum.shareit;

import java.util.List;

public interface Crud<T> {

    T create(T source);

    T retrieve(Long id);

    List<T> retrieve();

    List<T> retrieve(List<Long> ids);

    T update(T source);

    void delete(Long id);

}
