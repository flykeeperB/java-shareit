package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class BasicValidator<T> implements Validator<T> {

    protected void throwException(String message) {
        throw new ValidationException((getName() != null ? (getName() + ": ") : "") + " " + message);
    }

    protected void throwNotFoundException(String message) {
        throw new NotFoundException((getName() != null ? (getName() + ": ") : "") + " " + message);
    }

    protected void validateBeforeSave(T source, Optional<Long> userId) {
        if (source == null) {
            throwException("не передан элемент для сохрания в репозитории");
        }
    }

    public void validateUserId(Optional<Long> userId) {
        if (userId.isEmpty()) {
            throwException("Не задан идентификатор пользователя");
        }
    }

    @Override
    public void forCreate(T source, Optional<Long> userId) {
        validateBeforeSave(source, userId);
    }

    @Override
    public void forRetrieve(Long id, Optional<Long> userId) {
        if (id == null) {
            throwException("не передан идентификатор для получения элемента");
        }
    }

    @Override
    public void forRetrieve(Optional<Long> userId) {
        // Nothing to do
    }

    @Override
    public void forRetrieve(List<Long> ids, Optional<Long> userId) {
        if (ids == null) {
            throwException("не передан перечень идентификаторов для получения набора элементов");
        }
    }

    @Override
    public void forUpdate(T source, Optional<Long> userId) {
        validateBeforeSave(source, userId);
    }

    @Override
    public void forDelete(Long id, Optional<Long> userId) {
        if (id == null) {
            throwException("не передан идентификатор для удаления элемента");
        }
    }

    @Override
    public String getName() {
        return null;
    }
}
