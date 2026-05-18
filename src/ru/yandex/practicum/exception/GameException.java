package ru.yandex.practicum.exception;

abstract public class GameException extends RuntimeException {
    public GameException(final String message) {
        super(message);
    }
}
