package ru.yandex.practicum.exception;

public abstract class GameException extends RuntimeException {
    public GameException(final String message) {
        super(message);
    }
}
