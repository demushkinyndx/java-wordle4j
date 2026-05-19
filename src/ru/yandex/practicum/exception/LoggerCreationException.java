package ru.yandex.practicum.exception;

import java.io.IOException;

public class LoggerCreationException extends IOException {
    public LoggerCreationException(final String message) {
        super(message);
    }
}
