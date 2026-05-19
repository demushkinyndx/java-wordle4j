package ru.yandex.practicum;

import ru.yandex.practicum.exception.DictionaryIsEmptyException;
import ru.yandex.practicum.exception.GameException;
import ru.yandex.practicum.exception.StepsLimitExceededException;

import java.io.IOException;
import java.util.Scanner;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {

    private static Scanner scanner;
    private static final String DICTIONARY_FILE = "words_ru.txt";
    private static final int STEPS = 6; //кол-во попыток

    static void main(String[] args) {

        scanner = new Scanner(System.in);
        try {
            WordleGame game = new WordleGame(DICTIONARY_FILE, STEPS);
            game.startGame();
            System.out.println("Загадано слово из пяти букв (" + game.getHiddenWord() + "), у вас " + STEPS + " попыток.");

            while (true) {
                String word = getStringInput("Введите слово из 5 букв: ");
                try {
                    if (word == null) {
                        System.out.println(game.getSuggest());
                        continue;
                    }

                    if (game.isWordGuessed(word)) {
                        System.out.println("Вы победили. До свидания.");
                        break;
                    }

                    System.out.println(game.getAnswerSymbols());
                    System.out.println("Попыток осталось: " + game.getTries());
                } catch (StepsLimitExceededException limitException) {
                    System.out.println(limitException.getMessage());
                    break;
                }
            }
        } catch (IOException | DictionaryIsEmptyException e) {
            System.out.println(e.getMessage());
        }


    }

    public static String getStringInput(String message) {
        while (true) {
            if (message != null && !message.isEmpty()) {
                System.out.println(message);
            }
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                //считаем, что подсказку хотят
                return null;
            }

            if (input.length() != 5) {
                System.out.println("Нужно слово из 5 букв.");
                continue;
            }

            if (!input.matches("[а-яА-Я]+")) {
                System.out.println("Введите слово только из русских букв.");
                continue;
            }

            return input;
        }
    }

}
