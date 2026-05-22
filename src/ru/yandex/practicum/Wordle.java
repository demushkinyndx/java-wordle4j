package ru.yandex.practicum;

import ru.yandex.practicum.exception.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Wordle {

    private static final String DICTIONARY_FILE = "words_ru.txt";
    private static final String LOG_FILE = "log.txt";
    private static final int STEPS = 6;

    static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            try (FileOutputStream fos = new FileOutputStream(LOG_FILE);
                 Writer writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                 PrintWriter log = new PrintWriter(writer, true)) {
                try {
                    WordleGame game = new WordleGame(DICTIONARY_FILE, STEPS, log);
                    game.startGame();
                    System.out.println("Загадано слово из пяти букв, у вас " + STEPS + " попыток.");

                    while (true) {
                        String word = getStringInput("Введите слово из 5 букв (осталось попыток: " + game.getTries() + "): ", scanner);
                        try {
                            if (word == null) {
                                String suggest = game.getSuggest();
                                System.out.println(suggest != null ? suggest : "подсказка не найдена");

                                if (suggest.equals(game.getHiddenWord())) {
                                    //не особо понял по тз что делать в этом случае, пусть подсказки выиграют
                                    throw new SuggestWinException("Подсказки угадали слово. Игра окончена.");
                                }
                                continue;
                            }

                            if (game.isWordGuessed(word)) {
                                System.out.println("Вы победили. До свидания.");
                                break;
                            }

                            System.out.println(game.getAnswerSymbols());

                        } catch (StepsLimitExceededException | SuggestWinException ex) {
                            log.write(ex.getMessage());
                            System.out.println(ex.getMessage());
                            break;
                        } catch (WordNotFoundInDictionaryException ex) {
                            log.write(ex.getMessage());
                            System.out.println(ex.getMessage());
                        }
                    }
                } catch (RuntimeException | IOException e) {
                    e.printStackTrace(log);
                    System.out.println(e.getMessage());
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static String getStringInput(String message, Scanner scanner) {
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
