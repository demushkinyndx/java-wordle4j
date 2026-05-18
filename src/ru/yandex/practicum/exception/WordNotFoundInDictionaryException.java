package ru.yandex.practicum.exception;

public class WordNotFoundInDictionaryException extends GameException {
    public WordNotFoundInDictionaryException(final String message) {
        super(message);
    }
}
