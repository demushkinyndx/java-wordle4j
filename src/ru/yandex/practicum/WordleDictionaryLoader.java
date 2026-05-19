package ru.yandex.practicum;

import ru.yandex.practicum.exception.DictionaryIsEmptyException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {
    final private String dictionaryFile;

    public WordleDictionaryLoader(String dictionaryFile) {
        this.dictionaryFile = dictionaryFile;
    }

    public WordleDictionary loadWords() throws IOException, DictionaryIsEmptyException {
        WordleDictionary dictionary = new WordleDictionary();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(dictionaryFile))) {
            String word;
            while ((word = fileReader.readLine()) != null) {
                if (isValidWord(word)) {
                    dictionary.add(word.toLowerCase().replace('ё', 'е'));
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
