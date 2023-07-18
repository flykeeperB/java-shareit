package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class AbstractController<T extends AbstractDto, E extends AbstractModel> implements Controller<T> {

    private final Service<E> service;
    private final Mapper<E, T> mapper;

    @Override
    public T create(T source, Optional<Long> userId) {
        return mapper.toDto(service.create(mapper.fromDto(source)));
    }

    @Override
    public T retrieve(Long id, Optional<Long> userId) {
        return mapper.toDto(service.retrieve(id));
    }

    @Override
    public List<T> retrieve(Optional<Long> userId) {
        return mapper.toDto(service.retrieve());
    }

    @Override
    public List<T> retrieve(List<Long> ids, Optional<Long> userId) {
        return mapper.toDto(service.retrieve(ids));
    }

    @Override
    public T update(T source, Long id, Optional<Long> userId) {
        if (source == null) {
            return mapper.toDto(service.retrieve(id));
        }
        source.setId(id);
        return mapper.toDto(service.update(mapper.fromDto(source)));
    }

    @Override
    public void delete(Long id, Optional<Long> userId) {
        service.delete(id);
    }

    public Service<E> getService() {
        return service;
    }

}
