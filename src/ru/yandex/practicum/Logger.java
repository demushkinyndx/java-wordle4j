package ru.yandex.practicum;

import ru.yandex.practicum.exception.LoggerCreationException;
import ru.yandex.practicum.interfaces.LoggerInterface;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger implements AutoCloseable, LoggerInterface {
    private final PrintWriter infoWriter;
    private final PrintWriter errorWriter;
    private final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Logger(String infoFilename, String errorFilename) throws IOException {
        try {
            this.infoWriter = new PrintWriter(new FileWriter(infoFilename, true));
            this.errorWriter = new PrintWriter(new FileWriter(errorFilename, true));
        } catch (IOException e) {
            throw new LoggerCreationException("Ошибка создания логеров: " + e.getMessage());
        }
    }

    private String getTime() {
        return "[" + LocalDateTime.now().format(dtFormatter) + "] ";
    }

    public void error(Throwable throwable) {
        errorWriter.println(getTime() + throwable.toString());
        for (StackTraceElement element : throwable.getStackTrace()) {
            errorWriter.println("\tat " + element);
        }
        errorWriter.flush();
    }

    public void info(String message) {
        infoWriter.println(getTime() + message);
        infoWriter.flush();
    }

    @Override
    public void close() {
        infoWriter.close();
        errorWriter.close();
    }
}