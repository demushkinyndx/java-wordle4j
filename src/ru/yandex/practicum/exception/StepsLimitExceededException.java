package ru.yandex.practicum.exception;

public class StepsLimitExceededException extends GameException {
    public StepsLimitExceededException(String message) {
        super(message);
    }
}
