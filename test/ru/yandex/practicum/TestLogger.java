package ru.yandex.practicum;


import java.io.PrintWriter;
import java.io.Writer;

// Тестовый логгер вместо моков
class TestLogger extends PrintWriter  {

    public TestLogger(Writer out) {
        super(out);
    }

    @Override
    public void write(String message) {
        //null
    }
}