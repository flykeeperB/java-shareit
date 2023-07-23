package ru.practicum.shareit;

public interface Service<T extends AbstractModel, V extends Validator> extends Crud<T> {
    String getName();
}
