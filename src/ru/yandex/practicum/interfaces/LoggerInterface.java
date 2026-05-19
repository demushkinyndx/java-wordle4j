package ru.yandex.practicum.interfaces;

public interface LoggerInterface {
    public void info(String message);
    public void error(Throwable throwable);
}
