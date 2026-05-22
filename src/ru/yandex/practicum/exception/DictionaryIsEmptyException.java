package ru.yandex.practicum.exception;

public class DictionaryIsEmptyException extends RuntimeException {
    public DictionaryIsEmptyException(String message) {
        super(message);
    }
}
