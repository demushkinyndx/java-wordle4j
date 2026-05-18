package ru.yandex.practicum;

import ru.yandex.practicum.exception.DictionaryIsEmptyException;
import ru.yandex.practicum.exception.StepsLimitExceededException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private String suggest; //подсказка
    private String hiddenWord; //загаданное слово
    private int steps;

    private WordleDictionary dictionary;

    WordleGame(String dictionaryFile, int steps) throws IOException, DictionaryIsEmptyException {
        this.dictionary = (new WordleDictionaryLoader(dictionaryFile)).loadWords();
        this.steps = steps;
    }

    public String getHiddenWord() {
        return hiddenWord;
    }

    public void startGame() {
        //  hiddenWord = dictionary.getRandomWord();
        hiddenWord = "перец";
    }

    public boolean isWordGuessed(String word) {
        if (hiddenWord.equals(word)) {
            return true;
        }

        if (!dictionary.contains(word)) {
            //throw new WordNotFoundInDictionaryException("Такого слова вообще нет в словаре");
        }


        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < word.length(); i++) {
            String letter = String.valueOf(word.charAt(i));

            if (word.charAt(i) == hiddenWord.charAt(i)) {
                sb.append('+');
                continue;
            } else if (
                    word.charAt(i) != hiddenWord.charAt(i)
                            && hiddenWord.substring(i).contains(letter)
            ) {
                sb.append('^');
                continue;
            }
            sb.append('-');
        }
        answerSymbols = sb.toString();
        steps--;
        if (steps < 1) {
            throw new StepsLimitExceededException("Вы потратили все попытки и проиграли.");
        }
        return false;
    }


    public String getSuggest() {
        return null;
    }

    public String getAnswerSymbols() {
        return answerSymbols;
    }
}
