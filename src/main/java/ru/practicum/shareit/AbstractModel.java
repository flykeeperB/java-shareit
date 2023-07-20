package ru.practicum.shareit;

import lombok.Data;

import java.util.Objects;

@Data
public abstract class AbstractModel {
    protected Long id;

    public AbstractModel(AbstractModel source) {
        this.id = source.getId();
    }

    public AbstractModel() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractModel that = (AbstractModel) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
