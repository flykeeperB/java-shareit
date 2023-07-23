package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractService<T extends AbstractModel, V extends Validator<T>>
        implements Service<T> {

    private final ShareItRepository<T> repository;

    protected final V validator;

    protected void logInfo(String message) {
        log.info("Service (" + this.getName() + "): " + message);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public T create(T source, Optional<Long> userId) {
        logInfo("создание записи");

        validator.forCreate(source, userId);

        return repository.save(source);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public T retrieve(Long id, Optional<Long> userId) {
        logInfo("получение записи по идентификатору");

        validator.forRetrieve(id, userId);

        Optional<T> result = repository.findById(id);

        if (result.isEmpty()) {
            throw new NotFoundException("запись не найдена");
        }
        return result.get();
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<T> retrieve(Optional<Long> userId) {
        logInfo("получение записей");

        validator.forRetrieve(userId);

        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    public List<T> retrieve(List<Long> ids, Optional<Long> userId) {
        logInfo("получение записей по набору идентификаторов");

        validator.forRetrieve(ids, userId);

        return repository.findAllById(ids);
    }

    protected T patch(T source, T target) {
        target.setId(source.getId());

        return target;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public T update(T source, Optional<Long> userId) {
        logInfo("обновление записи");

        T target = patch(source, retrieve(source.getId(), userId));

        validator.forUpdate(source, userId);

        return repository.save(target);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Long id, Optional<Long> userId) {
        logInfo("удаление записи");

        validator.forDelete(id, userId);

        repository.deleteById(id);
    }

    public ShareItRepository<T> getRepository() {
        return repository;
    }
}
