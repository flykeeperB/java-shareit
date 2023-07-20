package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractController<T extends AbstractDto, E extends AbstractModel> implements Controller<T> {

    private final Service<E> service;
    private final Mapper<E, T> mapper;

    protected final String name;

    protected void logInfo(String message) {
        log.info("Controller (" + name + "): " + message);
    }

    @Override
    public T create(T source, Optional<Long> userId) {
        logInfo("запрос создания записи");
        return mapper.toDto(service.create(mapper.fromDto(source)));
    }

    @Override
    public T retrieve(Long id, Optional<Long> userId) {
        logInfo("запрос на получение записи по идентификатору");
        return mapper.toDto(service.retrieve(id));
    }

    @Override
    public List<T> retrieve(Optional<Long> userId) {
        logInfo("запрос на получение записей");
        return mapper.toDto(service.retrieve());
    }

    @Override
    public List<T> retrieve(List<Long> ids, Optional<Long> userId) {
        logInfo("запрос на получение записей по набору идентификаторов");
        return mapper.toDto(service.retrieve(ids));
    }

    @Override
    public T update(T source, Long id, Optional<Long> userId) {
        logInfo("запрос на обновление (редактирование) записи");
        if (source == null) {
            return mapper.toDto(service.retrieve(id));
        }
        source.setId(id);
        return mapper.toDto(service.update(mapper.fromDto(source)));
    }

    @Override
    public void delete(Long id, Optional<Long> userId) {
        logInfo("запрос на удаление записи");
        service.delete(id);
    }

    public Service<E> getService() {
        return service;
    }

}
