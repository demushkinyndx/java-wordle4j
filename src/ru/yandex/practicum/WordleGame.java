package ru.yandex.practicum;

import ru.yandex.practicum.exception.DictionaryIsEmptyException;
import ru.yandex.practicum.exception.StepsLimitExceededException;
import ru.yandex.practicum.exception.WordNotFoundInDictionaryException;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class WordleGame {

    private String answerSymbols;
    private String hiddenWord; //загаданное слово
    private int steps;
    private final WordleDictionary dictionary;
    private final PrintWriter log;

    private final Map<String, Integer> usedSuggestions = new LinkedHashMap<String, Integer>();
    private final Set<Character> notExistingCharSet = new HashSet<>();

    WordleGame(String dictionaryFile, int steps, PrintWriter log) throws IOException, DictionaryIsEmptyException {
        this.dictionary = (new WordleDictionaryLoader(dictionaryFile)).loadWords();
        this.steps = steps;
        this.log = log;
    }

    public String getHiddenWord() {
        return hiddenWord;
    }

    public void startGame() {
        hiddenWord = dictionary.getRandomWord();
        log.write("Начало игры, загадано слово: " + hiddenWord);
    }

    public boolean isWordGuessed(String word) {
        log.write("Введенное слово: " + word + " (ожидаем " + hiddenWord + "), осталось попыток: " + steps);

        if (hiddenWord.equals(word)) {
            log.write("Слово угадано");
            return true;
        }

        if (!dictionary.contains(word)) {
            throw new WordNotFoundInDictionaryException("Такого слова вообще нет в словаре");
        }

        StringBuilder sb = getStringBuilderSymbols(word);
        answerSymbols = sb.toString();

        log.write("символы слова: " + answerSymbols);

        steps--;
        if (steps < 1) {
            throw new StepsLimitExceededException("Вы потратили все попытки и проиграли.");
        }
        return false;
    }

    private StringBuilder getStringBuilderSymbols(String word) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < word.length(); i++) {
            char wordChar = word.charAt(i);
            String letter = String.valueOf(wordChar);

            if (word.charAt(i) == hiddenWord.charAt(i)) {
                sb.append('+');
                continue;
            }
            if (word.charAt(i) != hiddenWord.charAt(i) && hiddenWord.contains(letter)) {
                sb.append('^');
                continue;
            }

            sb.append('-');
            notExistingCharSet.add(wordChar);
        }
        return sb;
    }

    public boolean wordHasNotExistingChars(String word) {
        return word.chars().anyMatch(c -> notExistingCharSet.contains((char) c));
    }

    public String getSuggest() {
        List<String> allWords = dictionary.getAll();
        String exactMatch = null;
        List<String> suggestList = new ArrayList<>();
        String suggest;

        for (String word : allWords) {
            if (notExistingCharSet.isEmpty()) {
                return word;
            }

            if (usedSuggestions.containsKey(word)) {
                continue;
            }

            if (wordHasNotExistingChars(word)) {
                continue;
            }

            if (!hiddenWord.chars().allMatch(c -> word.contains(String.valueOf((char) c)))) {
                //в word нет букв из hiddenWord
                continue;
            }

            if (word.equals(hiddenWord)) {
                exactMatch = hiddenWord;
            } else {
                suggestList.add(word);
            }
        }
        if (exactMatch != null) {
            suggestList.add(exactMatch);
        }

        suggest = !suggestList.isEmpty() ? suggestList.getFirst() : null;
        if (suggest != null) {
            usedSuggestions.put(suggest, usedSuggestions.getOrDefault(suggest, 0) + 1);
            log.write("получена подсказка: " + suggest);
        } else {
            log.write("подсказки нет");
        }
        return suggest;
    }

    public int getTries() {
        return steps;
    }

    public String getAnswerSymbols() {
        return answerSymbols;
    }
}
