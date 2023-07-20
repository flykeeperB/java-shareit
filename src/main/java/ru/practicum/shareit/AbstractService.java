package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractService<T extends AbstractModel>
        implements Service<T> {

    private final Repository<T> repository;

    protected final String name;

    protected void logInfo(String message) {
        log.info("Service (" + name + "): " + message);
    }

    @Override
    public T create(T source) {
        logInfo("создание записи");
        return repository.create(source);
    }

    @Override
    public T retrieve(Long id) {
        logInfo("получение записи по идентификатору");
        return repository.retrieve(id);
    }

    @Override
    public List<T> retrieve() {
        logInfo("получение записей");
        return repository.retrieve();
    }

    @Override
    public List<T> retrieve(List<Long> ids) {
        logInfo("получение записей по набору идентификаторов");
        return repository.retrieve(ids);
    }

    @Override
    public T update(T source) {
        logInfo("обновление записи");
        return repository.update(source);
    }

    @Override
    public void delete(Long id) {
        logInfo("удаление записи");
        repository.delete(id);
    }

    public Repository<T> getRepository() {
        return repository;
    }
}
