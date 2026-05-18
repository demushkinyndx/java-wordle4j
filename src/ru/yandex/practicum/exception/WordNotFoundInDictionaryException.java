package ru.yandex.practicum.exception;

public class WordNotFoundInDictionaryException extends Exception {
    public WordNotFoundInDictionaryException(final String message) {
        super(message);
    }
}
