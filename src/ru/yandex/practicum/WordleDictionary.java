package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {

    private final List<String> words = new ArrayList<>();

    public List<String> getAll() {
        return words;
    }

    public void add(String word) {
        words.add(word);
    }

    public int size() {
        return words.size();
    }

    public String getRandomWord() {
        Random rand = new Random();
        int randomIndex = rand.nextInt(words.size());
        return words.get(randomIndex);
    }

    public boolean contains(String word) {
        return words.contains(word);
    }
}
