package ru.practicum.shareit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public abstract class InMemoryRepository<T extends AbstractModel>
        implements Repository<T> {

    @Getter
    private final String name;

    protected Map<Long, T> storage = new HashMap<>();
    protected Long lastId = 0L;

    private Long generateId() {
        return ++lastId;
    }

    private void validateId(Long id) {

        if (id == null) {
            throw new ValidationException(getName() + ": идентификатор не задан");
        }

        if (!storage.containsKey(id)) {
            throw new NotFoundException("Элемент с идентификатором (" + id + ") не найдена в репозитории (" + getName() + ")");
        }
    }

    protected void validateToSave(T source) {
        if (source == null) {
            throw new ValidationException("Не передан элемент для сохрания в репозитории (" + getName() + ")");
        }
    }

    @Override
    public T create(T source) {
        validateToSave(source);
        source.setId(generateId());

        storage.put(source.getId(), source);
        return storage.get(source.getId());
    }

    @Override
    public T retrieve(Long id) {
        validateId(id);

        return storage.get(id);
    }

    @Override
    public List<T> retrieve() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<T> retrieve(List<Long> ids) {
        List<T> result = new ArrayList<>();

        ids.forEach(id -> result.add(retrieve(id)));
        return result;
    }

    protected T patch(T source, T target) {
        target.setId(source.getId());

        return target;
    }

    @Override
    public T update(T source) {
        validateId(source.getId());

        T target = patch(source, retrieve(source.getId()));

        validateToSave(target);

        storage.replace(target.getId(), target);
        return retrieve(target.id);
    }

    @Override
    public void delete(Long id) {
        validateId(id);

        storage.remove(id);
    }
}
