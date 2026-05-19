package ru.yandex.practicum;

import ru.yandex.practicum.exception.DictionaryIsEmptyException;
import ru.yandex.practicum.exception.StepsLimitExceededException;
import ru.yandex.practicum.exception.SuggestWinException;
import ru.yandex.practicum.exception.WordNotFoundInDictionaryException;
import ru.yandex.practicum.interfaces.LoggerInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {

    private String answerSymbols;
    private String hiddenWord; //загаданное слово
    private int steps;
    private final WordleDictionary dictionary;
    private final LoggerInterface log;

    Map<String, Integer> usedSuggestions = new LinkedHashMap<String, Integer>();
    private final ArrayList<String> usedWords = new ArrayList<>();
    private final ArrayList<String> usedSymbols = new ArrayList<>();

    WordleGame(String dictionaryFile, int steps, LoggerInterface log) throws IOException, DictionaryIsEmptyException {
        this.dictionary = (new WordleDictionaryLoader(dictionaryFile)).loadWords();
        this.steps = steps;
        this.log = log;
    }

    public String getHiddenWord() {
        return hiddenWord;
    }

    public void startGame() {
        hiddenWord = dictionary.getRandomWord();
        log.info("Начало игры, загадано слово: " + hiddenWord);
    }

    public boolean isWordGuessed(String word) {
        log.info("Введенное слово: " + word + " (ожидаем " + hiddenWord + "), осталось попыток: " + steps);

        if (hiddenWord.equals(word)) {
            log.info("Слово угадано");
            return true;
        }

        if (!dictionary.contains(word)) {
            throw new WordNotFoundInDictionaryException("Такого слова вообще нет в словаре");
        }

        StringBuilder sb = getStringBuilderSymbols(word);
        answerSymbols = sb.toString();
        usedWords.add(word);
        usedSymbols.add(answerSymbols);

        log.info("символы слова: " + answerSymbols);

        steps--;
        if (steps < 1) {
            throw new StepsLimitExceededException("Вы потратили все попытки и проиграли.");
        }
        return false;
    }

    private StringBuilder getStringBuilderSymbols(String word) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < word.length(); i++) {
            String letter = String.valueOf(word.charAt(i));

            if (word.charAt(i) == hiddenWord.charAt(i)) {
                sb.append('+');
                continue;
            }
            if (word.charAt(i) != hiddenWord.charAt(i) && hiddenWord.contains(letter)) {
                sb.append('^');
                continue;
            }

            sb.append('-');
        }
        return sb;
    }


    public String getSuggest() {
        List<String> allWords = dictionary.getAll();

        for (String word : allWords) {
            if (usedSuggestions.containsKey(word)) {
                continue;
            }
            boolean valid = true;

            for (int t = 0; t < usedWords.size(); t++) {
                String used = usedWords.get(t);
                String mask = usedSymbols.get(t);

                for (int i = 0; i < word.length(); i++) {
                    char c = word.charAt(i);
                    char usedChar = used.charAt(i);
                    char symbol = mask.charAt(i);

                    if (symbol == '+') {
                        if (c != usedChar) {
                            valid = false;
                            break;
                        }
                        continue;
                    }
                    if (symbol == '^') {
                        if (c == usedChar || !word.contains("" + usedChar)) {
                            valid = false;
                            break;
                        }
                        continue;
                    }
                    if (symbol == '-') {
                        if (word.contains("" + usedChar)) {
                            valid = false;
                            break;
                        }
                    }
                }
                if (!valid) break;
            }

            if (valid) {
                usedSuggestions.put(word, usedSuggestions.getOrDefault(word, 0) + 1);
                if (word.equals(hiddenWord)) {
                    //не особо понял по тз что делать в этом случае, пусть подсказки выиграют
                    throw new SuggestWinException("Подсказки угадали слово. Игра окончена.");

                }
                log.info("получена подсказка: " + word);
                return word;
            }
        }
        log.info("не нашли подсказку, возможна проблема");
        return null;
    }

    public int getTries() {
        return steps;
    }

    public String getAnswerSymbols() {
        return answerSymbols;
    }
}
