package ru.yandex.practicum;

import ru.yandex.practicum.interfaces.LoggerInterface;

// Тестовый логгер вместо моков
class TestLogger implements LoggerInterface {
    @Override
    public void info(String message) {
    }

    @Override
    public void error(Throwable throwable) {
    }
}