package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlreadyExistException extends RuntimeException {
    public AlreadyExistException(String message) {
        super(message);
        log.error(message);
    }
}
