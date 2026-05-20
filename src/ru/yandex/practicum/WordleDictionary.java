package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordleDictionary {

    private final List<String> words = new ArrayList<>();

    public List<String> getAll() {
        return words;
    }

    public void add(String word) {
        words.add(word.toLowerCase().replace("ё", "е"));
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
