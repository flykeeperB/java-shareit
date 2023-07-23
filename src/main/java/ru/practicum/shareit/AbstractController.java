package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractController<T extends AbstractDto, E extends AbstractModel> implements Controller<T> {

    private final Service<E, ?> service;
    private final Mapper<E, T> mapper;

    protected void logInfo(String message) {
        log.info("Controller (" + this.getName() + "): " + message);
    }

    @Override
    public T create(T source, Optional<Long> userId) {
        logInfo("запрос создания записи");
        return mapper.toDto(service.create(mapper.fromDto(source), userId));
    }

    @Override
    public T retrieve(Long id, Optional<Long> userId) {
        logInfo("запрос на получение записи по идентификатору");
        return mapper.toDto(service.retrieve(id, userId));
    }

    @Override
    public List<T> retrieve(Optional<Long> userId) {
        logInfo("запрос на получение записей");
        return mapper.toDto(service.retrieve(userId));
    }

    @Override
    public List<T> retrieve(List<Long> ids, Optional<Long> userId) {
        logInfo("запрос на получение записей по набору идентификаторов");
        return mapper.toDto(service.retrieve(ids, userId));
    }

    @Override
    public T update(T source, Long id, Optional<Long> userId) {
        logInfo("запрос на обновление (редактирование) записи");
        if (source == null) {
            return mapper.toDto(service.retrieve(id, userId));
        }

        source.setId(id);
        return this.update(source, userId);
    }

    @Override
    public T update(T source, Optional<Long> userId) {
        return mapper.toDto(service.update(mapper.fromDto(source), userId));
    }

    @Override
    public void delete(Long id, Optional<Long> userId) {
        logInfo("запрос на удаление записи");
        service.delete(id, userId);
    }

    public Service<E, ?> getService() {
        return service;
    }

}
