package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public abstract class AbstractService
        <T extends AbstractModel>
        implements Service<T> {

    private final Repository<T> repository;

    @Override
    public T create(T source) {
        return repository.create(source);
    }

    @Override
    public T retrieve(Long id) {
        return repository.retrieve(id);
    }

    @Override
    public List<T> retrieve() {
        return repository.retrieve();
    }

    @Override
    public List<T> retrieve(List<Long> ids) {
        return repository.retrieve(ids);
    }

    @Override
    public T update(T source) {
        return repository.update(source);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }

    public Repository<T> getRepository() {
        return repository;
    }
}
