package ru.practicum.shareit;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractMapper<T extends AbstractModel, E extends AbstractDto>
        implements Mapper<T,E>{

    @Override
    public List<E> toDto(List<T> source) {
        return source
                .stream()
                .map(model->this.toDto(model))
                .collect(Collectors.toList());
    }
}
