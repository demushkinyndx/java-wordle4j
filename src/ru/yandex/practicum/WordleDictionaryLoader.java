package ru.yandex.practicum;

import ru.yandex.practicum.exception.DictionaryIsEmptyException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class WordleDictionaryLoader {
    private final String dictionaryFile;

    public WordleDictionaryLoader(String dictionaryFile) {
        this.dictionaryFile = dictionaryFile;
    }

    public WordleDictionary loadWords() throws IOException, DictionaryIsEmptyException {
        WordleDictionary dictionary = new WordleDictionary();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(dictionaryFile))) {
            String word;
            while ((word = fileReader.readLine()) != null) {
                if (isValidWord(word)) {
                    dictionary.add(word);
                }
            }

        } catch (Throwable e) {
            throw new IOException(e);
        }
        if (dictionary.size() == 0) {
            throw new DictionaryIsEmptyException("Ошибка. Словарь пуст");
        }
        return dictionary;
    }

    private boolean isValidWord(String word) {
        return word.length() == 5 && word.matches("[а-яё]+");
    }
}
